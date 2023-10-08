package com.nova.tools;

import com.nova.tools.demo.springboot.TestApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wzh
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.nova.common", "com.nova.tools"})
public class ToolsApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ToolsApplication.class);
        application.addInitializers(new TestApplicationContextInitializer());
        application.run(args);
    }

}
