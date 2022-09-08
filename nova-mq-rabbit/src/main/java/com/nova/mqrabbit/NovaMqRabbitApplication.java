package com.nova.mqrabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NovaMqRabbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaMqRabbitApplication.class, args);
    }

}
