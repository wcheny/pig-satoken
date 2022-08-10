package com.wangchenyang.gateway.config;

import com.wangchenyang.gateway.filter.PasswordDecoderFilter;
import com.wangchenyang.gateway.filter.ValidateCodeGatewayFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * @author lengleng
 * @date 2020/10/4
 * <p>
 * 网关配置文件
 */
@Data
@RefreshScope
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {

	/**
	 * 网关解密登录前端密码 秘钥 {@link PasswordDecoderFilter}
	 */
	private String encodeKey;

	/**
	 * 网关不需要校验验证码的客户端 {@link ValidateCodeGatewayFilter}
	 */
	private List<String> ignoreClients;

}
