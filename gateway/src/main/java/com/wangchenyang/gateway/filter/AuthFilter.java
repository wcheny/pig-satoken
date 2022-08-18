package com.wangchenyang.gateway.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.gateway.config.properties.IgnoreWhiteProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 拦截器
 *
 */
@Configuration
public class AuthFilter {

	/**
	 * 注册 Sa-Token 全局过滤器
	 */
	@Bean
	public SaReactorFilter getSaReactorFilter(IgnoreWhiteProperties ignoreWhite) {
		return new SaReactorFilter()
				// 拦截地址
				.addInclude("/**").addExclude("/actuator/**")
				// 鉴权方法：每次访问进入
				.setAuth(obj -> {
					// 登录校验 -- 拦截所有路由
					SaRouter.match("/**").notMatch(ignoreWhite.getWhites()).check(r -> {
						// 检查是否登录 是否有token
						StpUtil.checkLogin();
					});
				}).setError(e -> {
					SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
					SaHolder.getResponse().setStatus(424);
					// 使用封装的 JSON 工具类转换数据格式
					return JSONUtil.toJsonStr(R.failed("认证失败，无法访问系统资源"));
				});
	}

}
