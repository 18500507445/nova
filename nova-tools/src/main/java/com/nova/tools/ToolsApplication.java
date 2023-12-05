package com.nova.tools;

import com.nova.tools.demo.springboot.TestApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wzh
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.nova.common", "com.nova.tools"})
//开启spring异步支持
@EnableAsync
//开启spring重试
@EnableRetry
public class ToolsApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ToolsApplication.class);
        application.addInitializers(new TestApplicationContextInitializer());
        application.run(args);
    }

}
