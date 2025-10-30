package com.nova.orm.mongoplus;

import com.mongoplus.annotation.MongoMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description: mongo-plus启动类
 * @date: 2025/10/30 16:39
 */
@MongoMapperScan("com.nova.orm.mongoplus.repository")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MongoPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoPlusApplication.class, args);
    }
}
