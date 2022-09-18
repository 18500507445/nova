package com.nova.design;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NovaDesignApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaDesignApplication.class, args);
    }

}
