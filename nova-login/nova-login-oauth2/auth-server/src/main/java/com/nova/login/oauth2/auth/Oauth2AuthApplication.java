package com.nova.login.oauth2.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description: oauth2启动类
 * @date: 2024/12/24 10:11
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Oauth2AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2AuthApplication.class, args);
    }
}
