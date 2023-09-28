package com.nova.login.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: wzh
 * @description sa-token配置类
 * @date: 2023/09/28 14:07
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    // 注册Sa-Token拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    /**
     * Simple 简单模式 new StpLogicJwtForSimple();
     * Mixin 混入模式 new StpLogicJwtForMixin();
     * Stateless 无状态模式 new StpLogicJwtForStateless();
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}
