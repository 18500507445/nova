package com.nova.spring.config;

import com.nova.spring.entity.Card;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @description: 自动化配置
 * @author: wangzehui
 * @date: 2022/12/29 17:21
 */
@Configuration
public class MainConfiguration {

    /**
     * 默认单例，原型模式 @Scope("prototype")
     * @return
     */
    @Bean
    @Scope("prototype")
    public Card card() {
        return new Card();
    }

}
