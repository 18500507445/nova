package com.nova.excel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description: excel启动类
 * @author: wzh
 * @date: 2022/9/8 19:43
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.nova.common", "com.nova.excel"})
public class ExcelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelApplication.class, args);
    }

}
