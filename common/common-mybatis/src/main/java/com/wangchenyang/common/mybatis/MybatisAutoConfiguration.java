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

package com.wangchenyang.common.mybatis;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.wangchenyang.common.mybatis.config.MybatisPlusMetaObjectHandler;
import com.wangchenyang.common.mybatis.plugins.DecryptFieldInterceptor;
import com.wangchenyang.common.mybatis.plugins.EncryptFieldInterceptor;
import com.wangchenyang.common.mybatis.plugins.YifanPaginationInnerInterceptor;
import com.wangchenyang.common.mybatis.resolver.SqlFilterArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author lengleng
 * @date 2020-03-14
 * <p>
 * mybatis plus 统一配置
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
public class MybatisAutoConfiguration implements WebMvcConfigurer {

	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return configuration -> {
			// 注入参数入库加解密拦截器
			configuration.addInterceptor(new EncryptFieldInterceptor());
			configuration.addInterceptor(new DecryptFieldInterceptor());
		};
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	/**
	 * SQL 过滤器避免SQL 注入
	 * @param argumentResolvers
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new SqlFilterArgumentResolver());
	}

	/**
	 * 分页插件, 对于单一数据库类型来说,都建议配置该值,避免每次分页都去抓取数据库类型
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new YifanPaginationInnerInterceptor());
		return interceptor;
	}

	/**
	 * 审计字段自动填充
	 * @return {@link MetaObjectHandler}
	 */
	@Bean
	public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
		return new MybatisPlusMetaObjectHandler();
	}

}
