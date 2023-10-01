package com.nova.orm.mybatisflex;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description ${description}
 * @date: 2023/07/30 14:11
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MybatisFlexApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisFlexApplication.class, args);
    }

}