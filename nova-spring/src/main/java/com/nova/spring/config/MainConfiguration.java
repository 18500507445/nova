package com.nova.spring.config;

import com.nova.spring.entity.Card;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 自动化配置
 * @author: wangzehui
 * @date: 2022/12/29 17:21
 */
@Configuration
public class MainConfiguration {

    @Bean
    public Card card() {
        return new Card();
    }

}
