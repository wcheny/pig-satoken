package com.wangchenyang.yifan.jimu.config;

import com.wangchenyang.yifan.jimu.service.JmReportTokenServiceImpl;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 积木报表的配置类
 *
 */
@Configuration
public class JmReportConfiguration {

    @Bean
    public JmReportTokenServiceI jmReportTokenService() {
        return new JmReportTokenServiceImpl();
    }

}
