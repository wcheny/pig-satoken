package com.wangchenyang.common.redis.config;


import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangchenyang.common.redis.config.properties.RedisConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * redis配置
 *
 * @author Lion Li
 */
@Slf4j
@AutoConfiguration
@EnableCaching
@EnableConfigurationProperties(RedisConfigProperties.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfiguration {


	@Autowired
	private RedisConfigProperties redisConfigProperties;
	@Autowired
	private ObjectMapper objectMapper;

	//添加redisson的bean
	@Bean
	public RedissonClient redisson() {
		String redissonAddr = "redis://" + redisConfigProperties.getHost()+":"+redisConfigProperties.getPort();
		Config config = new Config();
		config.setCodec(new JsonJacksonCodec(objectMapper));
		config.useSingleServer().setAddress(redissonAddr)
				.setPassword(redisConfigProperties.getPassword());
		return Redisson.create(config);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setHashKeySerializer(RedisSerializer.string());
		redisTemplate.setValueSerializer(RedisSerializer.json());
		redisTemplate.setHashValueSerializer(RedisSerializer.json());
		redisTemplate.setConnectionFactory(factory);
		return redisTemplate;
	}


	@Bean
	public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.computePrefixWith(name -> name + ":")
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
		return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
	}
}
