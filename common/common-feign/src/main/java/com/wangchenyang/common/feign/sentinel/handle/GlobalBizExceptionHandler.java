/*
 * Copyright (c) 2020 yifan4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wangchenyang.common.feign.sentinel.handle;

import cn.dev33.satoken.exception.IdTokenInvalidException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.alibaba.csp.sentinel.Tracer;
import com.wangchenyang.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 全局异常处理器结合sentinel 全局异常处理器不能作用在 oauth server https://gitee.com/log4j/yifan/issues/I1M2TJ
 * </p>
 *
 * @author lengleng
 * @date 2020-06-29
 */
@Slf4j
@Order(10000)
@RestControllerAdvice
public class GlobalBizExceptionHandler {

	/**
	 * 权限码异常
	 */
	@ExceptionHandler(NotPermissionException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求地址'{}',权限码校验失败'{}'", requestURI, e.getMessage());
		return R.failed("没有访问权限，请联系管理员授权");
	}

	/**
	 * 角色权限异常
	 */
	@ExceptionHandler(NotRoleException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Void> handleNotRoleException(NotRoleException e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求地址'{}',角色权限校验失败'{}'", requestURI, e.getMessage());
		return R.failed("没有访问权限，请联系管理员授权");
	}

	/**
	 * 认证失败
	 */
	@ExceptionHandler(NotLoginException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Void> handleNotLoginException(NotLoginException e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求地址'{}',认证失败'{}',无法访问系统资源", requestURI, e.getMessage());
		return R.failed("认证失败，无法访问系统资源");
	}

	/**
	 * 无效认证
	 */
	@ExceptionHandler(IdTokenInvalidException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Void> handleIdTokenInvalidException(IdTokenInvalidException e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求地址'{}',内网认证失败'{}',无法访问系统资源", requestURI, e.getMessage());
		return R.failed("认证失败，无法访问系统资源");
	}

	/**
	 * 全局异常.
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleGlobalException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);

		// 业务异常交由 sentinel 记录
		Tracer.trace(e);
		return R.failed(e.getLocalizedMessage());
	}

	/**
	 * 处理业务校验过程中碰到的非法参数异常 该异常基本由{@link org.springframework.util.Assert}抛出
	 * @see Assert#hasLength(String, String)
	 * @see Assert#hasText(String, String)
	 * @see Assert#isTrue(boolean, String)
	 * @see Assert#isNull(Object, String)
	 * @see Assert#notNull(Object, String)
	 * @param exception 参数校验异常
	 * @return API返回结果对象包装后的错误输出结果
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.OK)
	public R handleIllegalArgumentException(IllegalArgumentException exception) {
		log.error("非法参数,ex = {}", exception.getMessage(), exception);
		return R.failed(exception.getMessage());
	}

	/**
	 * validation Exception
	 * @param exception
	 * @return R
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleBodyValidException(MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return R.failed(fieldErrors.get(0).getDefaultMessage());
	}

	/**
	 * validation Exception (以form-data形式传参)
	 * @param exception
	 * @return R
	 */
	@ExceptionHandler({ BindException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R bindExceptionHandler(BindException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return R.failed(fieldErrors.get(0).getDefaultMessage());
	}

}
