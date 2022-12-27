package com.nova.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NovaLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaLockApplication.class, args);
    }

}
