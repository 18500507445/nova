package com.nova.log;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class LogApplication {

    public static void main(String[] args) {
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        new SpringApplicationBuilder(LogApplication.class)
                .web(WebApplicationType.SERVLET)
                .initializers((ConfigurableApplicationContext context) -> System.setProperty("appName", Objects.requireNonNull(context.getEnvironment().getProperty("spring.application.name"))))
                .run(args);

    }

}
