package com.nova.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NovaLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaLogApplication.class, args);
    }

}
