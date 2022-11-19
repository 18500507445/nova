package com.nova.limit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NovaLimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaLimitApplication.class, args);
    }

}
