package com.nova.mqactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NovaMqActiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaMqActiveApplication.class, args);
    }

}
