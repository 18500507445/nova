package com.nova.limit.config;

import com.nova.limit.interceptor.LimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/11/19 17:18
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private LimitInterceptor limitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limitInterceptor);
    }
}
