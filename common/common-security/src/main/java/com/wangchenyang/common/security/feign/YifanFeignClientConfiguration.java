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

package com.wangchenyang.common.security.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class YifanFeignClientConfiguration {

	/**
	 * 注入 oauth2 feign token 增强
	 * @return 拦截器
	 */
	@Bean
	public RequestInterceptor oauthRequestInterceptor() {
		return new YifanOAuthRequestInterceptor();
	}

}
