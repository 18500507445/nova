package com.nova.mq.active;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/21 09:50
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ActiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActiveApplication.class, args);
    }

}
