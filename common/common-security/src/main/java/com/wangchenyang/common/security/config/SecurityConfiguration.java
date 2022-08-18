package com.wangchenyang.common.security.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.id.SaIdUtil;
import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.hutool.json.JSONUtil;
import com.wangchenyang.common.core.util.R;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 权限安全配置
 */
@AutoConfiguration
@Component
public class SecurityConfiguration implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注解拦截器
		registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
	}

	/**
	 * 校验是否从网关转发
	 */
	@Bean
	public SaServletFilter getSaServletFilter() {
		return new SaServletFilter().addInclude("/**").addExclude("/actuator/**").setAuth(obj -> {
			SaRouter.match("/**").check(SaIdUtil::checkCurrentRequestToken);
		}).setError(e -> {
			SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
			SaHolder.getResponse().setStatus(424);
			// 使用封装的 JSON 工具类转换数据格式
			return JSONUtil.toJsonStr(R.failed("认证失败，无法访问系统资源"));
		});
	}

}
