package com.wangchenyang.common.anti.replay.props;

import lombok.Data;
import online.inote.naruto.common.utils.bean.BeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author XQF.Sui
 * @description JWT属性配置
 * @date 2021/07/21 18:01
 */
@Data
@Component
@ConfigurationProperties(prefix = "anti.replay")
public class ReplayProperties {

	/**
	 * Request Header信息对象
	 */
	private HeaderKey headerKey = new HeaderKey();

	/**
	 * 请求配置
	 */
	private Request request = new Request();

	/**
	 * 缓存配置
	 */
	private Cache cache = new Cache();

	private SignatureAlgorithm signatureAlgorithm = new SignatureAlgorithm();

	@Data
	public class HeaderKey {

		/**
		 * 请求ID 防止重放
		 */
		private String nonce = "nonce";

		/**
		 * 请求时间 避免缓存时间过后重放
		 */
		private String timestamp = "timestamp";

		/**
		 * 请求URL
		 */
		private String url = "url";

		/**
		 * Token
		 */
		private String token = "token";

		/**
		 * 签名摘要
		 */
		private String signature = "signature";

	}

	@Data
	public class Request {

		/**
		 * 请求有效期
		 */
		private Long expireTime = 60L;

	}

	@Data
	public class Cache {

		/**
		 * 缓存Key前缀
		 */
		private String cacheKeyPrefix = "PIG:SECURITY:ANTI-REPLAY:REQUEST_ID_";

		/**
		 * 锁持续时间(避免异常造成锁不释放)
		 */
		private long lockHoldTime = 300L;

	}

	@Data
	public class SignatureAlgorithm {

		private String salt = "PIG4CLOUD";

	}

	public static ReplayProperties props() {
		return BeanFactory.getBean(ReplayProperties.class);
	}

}
