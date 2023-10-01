package com.nova.log.logback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description 配置类
 * @date: 2023/08/01 16:18
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private LogInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
