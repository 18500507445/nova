package com.starter.mongo;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/22 20:53
 */
@AutoConfiguration
public class MongoAutoConfiguration {

    @Bean(name = "mongoService")
    public MongoService mongoService() {
        return new MongoService();
    }
}
