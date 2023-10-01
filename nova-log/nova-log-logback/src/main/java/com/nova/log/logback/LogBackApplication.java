package com.nova.log.logback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description: logback
 * @author: wzh
 * @date: 2022/11/19 17:19
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.nova.common", "com.nova.log.logback"})
public class LogBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogBackApplication.class, args);
    }

}
