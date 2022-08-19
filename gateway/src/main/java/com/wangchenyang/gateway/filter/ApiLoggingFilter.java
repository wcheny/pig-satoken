package com.wangchenyang.gateway.filter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.wangchenyang.gateway.config.properties.CustomGatewayProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

/**
 * @author zhangran
 * @date 2021/7/13
 * <p>
 * 全局拦截器，作用所有的微服务
 * <p>
 * 1. 对请求的API调用过滤，记录接口的请求时间，方便日志审计、告警、分析等运维操作 2. 后期可以扩展对接其他日志系统
 * <p>
 */
@Slf4j
@Component
public class ApiLoggingFilter implements GlobalFilter, Ordered {

	private static final String START_TIME = "startTime";

	private static final String X_REAL_IP = "X-Real-IP";// nginx需要配置

	@Autowired
	private CustomGatewayProperties customGatewayProperties;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		if (!customGatewayProperties.getRequestLog()) {
			return chain.filter(exchange);
		}
		ServerHttpRequest request = exchange.getRequest();
		String url = request.getMethod().name() + " " + request.getURI().getPath();
		log.debug("开始请求 => URL:[{}],参数:[{}]", url, request.getQueryParams());
		exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			Long startTime = exchange.getAttribute(START_TIME);
			if (startTime != null) {
				Long executeTime = (System.currentTimeMillis() - startTime);
				List<String> ips = exchange.getRequest().getHeaders().get(X_REAL_IP);
				String ip = ips != null ? ips.get(0) : null;
				String api = exchange.getRequest().getURI().getRawPath();
				int code = 500;
				if (exchange.getResponse().getStatusCode() != null) {
					code = exchange.getResponse().getStatusCode().value();
				}
				log.debug("结束请求 => IP地址[{}],URL[{}],响应状态码：[{}],耗时:[{}]毫秒", ip, api, code, executeTime);
			}
		}));
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
