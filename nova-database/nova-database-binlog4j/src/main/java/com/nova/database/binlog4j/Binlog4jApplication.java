package com.nova.database.binlog4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @description: binlog4j启动类，支持宕机续读
 * @author: wzh
 * @date: 2024/07/30 13:57
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Binlog4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(Binlog4jApplication.class, args);
    }

}