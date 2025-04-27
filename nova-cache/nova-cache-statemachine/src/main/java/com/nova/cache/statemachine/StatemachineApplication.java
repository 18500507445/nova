package com.nova.cache.statemachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: wzh
 * @description: spring状态机启动类
 * @date: 2024/04/11 14:52
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.nova.common", "com.nova.cache.statemachine"})
public class StatemachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatemachineApplication.class, args);
    }

}
