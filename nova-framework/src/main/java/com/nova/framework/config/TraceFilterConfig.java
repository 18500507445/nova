package com.nova.framework.config;

import com.nova.common.trace.TraceWebFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Description: 配置Trace filter
 * @Author: wangzehui
 * @Date: 2022/12/20 11:16
 */
@Configuration
public class TraceFilterConfig {

    @Resource
    private TraceWebFilter traceWebFilter;

    @Bean
    public FilterRegistrationBean<TraceWebFilter> registerTraceFilter() {
        FilterRegistrationBean<TraceWebFilter> registration = new FilterRegistrationBean<TraceWebFilter>();
        registration.setFilter(traceWebFilter);
        registration.addUrlPatterns("/*");
        registration.setName("traceWebFilter");
        registration.setOrder(1);
        return registration;
    }
}
