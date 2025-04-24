package com.nova.orm.mybatisplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description: ${description}
 * @date: 2023/07/13 14:11
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MybatisPlusApplication {

    public static void main(String[] args) {
        //关闭pageHelper启动banner图
        System.setProperty("pagehelper.banner", "false");
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

}