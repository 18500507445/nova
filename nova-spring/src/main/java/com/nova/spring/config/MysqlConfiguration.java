package com.nova.spring.config;

import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/12/29 20:31
 */
public class MysqlConfiguration {

    @Bean
    public Connection getConnection() throws SQLException {
        System.out.println("创建新的连接！");
        return DriverManager.getConnection("jdbc:mysql://47.100.174.176:3306/pay_center",
                "root",
                "@wangzehui123");
    }
}
