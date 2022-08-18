package com.wangchenyang.common.redis.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisConfigProperties {

	private String password;
	private String database;
	private int port=6379;
	private String host;

}
