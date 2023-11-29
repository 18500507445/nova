package com.nova.mq.rocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description Rocket启动类
 * @date: 2023/07/13 14:00
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketApplication.class, args);
    }
}