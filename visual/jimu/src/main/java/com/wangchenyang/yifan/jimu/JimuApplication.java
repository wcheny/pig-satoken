package com.wangchenyang.yifan.jimu;

import com.wangchenyang.common.feign.annotation.EnableYifanFeignClients;
import com.wangchenyang.common.security.annotation.EnableYifanResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
* @author pig archetype
* <p>
* 项目启动类
*/
@EnableYifanResourceServer
@EnableYifanFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class JimuApplication {

    public static void main(String[] args) {
        SpringApplication.run(JimuApplication.class, args);
    }

}
