package com.nova.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NovaCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaCacheApplication.class, args);
    }

}
