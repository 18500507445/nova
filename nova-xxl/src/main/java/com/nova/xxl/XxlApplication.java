package com.nova.xxl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author wzh
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class XxlApplication {

    public static void main(String[] args) {
        SpringApplication.run(XxlApplication.class, args);
    }

}
