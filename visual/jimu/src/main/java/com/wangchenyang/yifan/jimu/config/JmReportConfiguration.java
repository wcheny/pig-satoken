package com.wangchenyang.yifan.jimu.config;

import com.wangchenyang.yifan.jimu.service.JmReportTokenServiceImpl;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**
 * 积木报表的配置类
 *
 * @author 芋道源码
 */
@Configuration
public class JmReportConfiguration {

    @Bean
    public JmReportTokenServiceI jmReportTokenService(OAuth2AuthorizationService authorizationService) {
        return new JmReportTokenServiceImpl(authorizationService);
    }

}
