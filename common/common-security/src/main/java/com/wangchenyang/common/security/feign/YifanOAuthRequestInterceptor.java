package com.wangchenyang.common.security.feign;

import cn.dev33.satoken.id.SaIdUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.wangchenyang.common.core.constant.SecurityConstants;
import com.wangchenyang.common.core.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Enumeration;

/**
 * oauth2 feign token传递
 *
 * 重新 OAuth2FeignRequestInterceptor ，官方实现部分常见不适用
 *
 * @author lengleng
 * @date 2022/5/29
 */
@Slf4j
@RequiredArgsConstructor
public class YifanOAuthRequestInterceptor implements RequestInterceptor {

	/**
	 * Create a template with the header of provided name and extracted extract </br>
	 *
	 * 1. 如果使用 非web 请求，header 区别 </br>
	 *
	 * 2. 根据authentication 还原请求token
	 * @param template
	 */
	@Override
	public void apply(RequestTemplate template) {
		template.header(SaIdUtil.ID_TOKEN, SaIdUtil.getToken());
	}

}
