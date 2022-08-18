package com.wangchenyang.gateway.config.properties;

import com.wangchenyang.gateway.filter.PasswordDecoderFilter;
import com.wangchenyang.gateway.filter.ValidateCodeGatewayFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lengleng
 * @date 2020/10/4
 * <p>
 * 网关配置文件
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties("security.encode")
public class GatewayConfigProperties {

	/**
	 * 网关解密登录前端密码 秘钥 {@link PasswordDecoderFilter}
	 */
	private String encodeKey;

	/**
	 * 是否校验验证码
	 *
	 * @author 王晨阳
	 * @version 1.0
	 * @date 2022/8/18 20:37
	 * @desc
	 **/
	private boolean validateCode;

}
