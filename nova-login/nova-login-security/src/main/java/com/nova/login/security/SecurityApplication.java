package com.nova.login.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description 启动类
 * @date: 2023/10/09 20:12
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
}
