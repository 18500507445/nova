package com.nova.log.log4j2;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Objects;

/**
 * @description: log4j2
 * @author: wzh
 * @date: 2022/11/19 17:19
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.nova.common", "com.nova.log.log4j2"})
public class Log4j2Application {

    public static void main(String[] args) {
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        new SpringApplicationBuilder(Log4j2Application.class)
                .web(WebApplicationType.SERVLET)
                .initializers((ConfigurableApplicationContext context) -> System.setProperty("appName", Objects.requireNonNull(context.getEnvironment().getProperty("spring.application.name"))))
                .run(args);

    }

}
