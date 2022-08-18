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

package com.wangchenyang.gateway.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangchenyang.common.core.constant.CacheConstants;
import com.wangchenyang.common.core.exception.ValidateCodeException;
import com.wangchenyang.common.redis.utils.RedisUtils;
import com.wangchenyang.gateway.config.properties.GatewayConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * The type Validate code gateway filter.
 *
 * @authoC lengleng
 * @date 2018 /7/4 验证码处理
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ValidateCodeGatewayFilter extends AbstractGatewayFilterFactory<Object> {

	private final GatewayConfigProperties gatewayConfig;

	private final static String[] URL = new String[]{"/auth/login"};

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			// 非登录请求，不处理
			if (StrUtil.equalsAnyIgnoreCase(request.getURI().getPath(), URL)&&gatewayConfig.isValidateCode()) {
				checkCode(request);
			}
			return chain.filter(exchange);
		};
	}

	private void checkCode(ServerHttpRequest request) {
		String code = request.getQueryParams().getFirst("code");

		if (CharSequenceUtil.isBlank(code)) {
			throw new ValidateCodeException("验证码不能为空");
		}

		String randomStr = request.getQueryParams().getFirst("randomStr");

		String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;
		String codeObj = RedisUtils.getCacheObject(key);
		RedisUtils.deleteObject(key);

		if (ObjectUtil.isEmpty(codeObj) || !code.equals(codeObj)) {
			throw new ValidateCodeException("验证码错误");
		}
	}

}
