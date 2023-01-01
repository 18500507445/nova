package com.nova.limit.config;

import com.nova.limit.interceptor.LimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @description: 配置
 * @author: wzh
 * @date: 2022/11/19 17:18
 */
@Configuration
public class LimitConfig implements WebMvcConfigurer {

    @Resource
    private LimitInterceptor limitInterceptor;

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limitInterceptor);
    }
}
