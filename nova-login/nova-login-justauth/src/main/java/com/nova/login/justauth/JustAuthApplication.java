package com.nova.login.justauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @description: JustAuth
 * @see <a href="https://www.justauth.cn">JustAuth</a>
 * @author: wzh
 * @date: 2022/9/8 19:43
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class JustAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(JustAuthApplication.class, args);
    }

}
