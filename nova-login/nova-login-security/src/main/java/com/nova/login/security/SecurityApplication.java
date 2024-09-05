package com.nova.login.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author: wzh
 * @description 启动类
 * @date: 2023/10/09 20:12
 */
@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SecurityApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SecurityApplication.class, args);
        String name = applicationContext.getEnvironment().getProperty("spring.security.user.name");
        String password = applicationContext.getEnvironment().getProperty("spring.security.user.password");

        log.warn("【security地址 =====> http://localhost:8080/login】");
        log.warn("【账号：{}，密码：{}】", name, password);
    }
}
