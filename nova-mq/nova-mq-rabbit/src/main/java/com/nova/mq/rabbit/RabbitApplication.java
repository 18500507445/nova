package com.nova.mq.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @description: rabbitMQ启动类
 * @author: wzh
 * @date: 2023/4/21 09:50
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RabbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitApplication.class, args);
    }

}
