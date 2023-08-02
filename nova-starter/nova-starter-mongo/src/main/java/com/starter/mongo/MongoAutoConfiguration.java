package com.starter.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/22 20:53
 */
@Configuration
public class MongoAutoConfiguration {

    @Bean(name = "mongoService")
    public MongoService mongoService() {
        return new MongoService();
    }
}
