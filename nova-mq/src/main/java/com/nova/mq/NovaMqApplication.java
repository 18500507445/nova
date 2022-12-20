package com.nova.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NovaMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaMqApplication.class, args);
    }

}
