package com.nova.common.trace;

import com.nova.common.trace.TraceFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @description: TraceFilter配置
 * @author: wzh
 * @date: 2022/12/20 11:16
 */
@Configuration
public class TraceConfig {

    @Resource
    private TraceFilter traceFilter;

    @Bean
    public FilterRegistrationBean<TraceFilter> registerTraceFilter() {
        FilterRegistrationBean<TraceFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(traceFilter);
        registration.addUrlPatterns("/*");
        registration.setName("traceWebFilter");
        registration.setOrder(1);
        return registration;
    }
}
