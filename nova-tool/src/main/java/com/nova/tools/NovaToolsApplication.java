package com.nova.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NovaToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaToolsApplication.class, args);
    }

}
