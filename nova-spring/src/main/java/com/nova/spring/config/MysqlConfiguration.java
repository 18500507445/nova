package com.nova.spring.config;

import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @description:
 * @author: wzh
 * @date: 2022/12/29 20:31
 */
public class MysqlConfiguration {

    /**
     * 获取jdbc连接
     * @return
     * @throws SQLException
     */
    @Bean
    public Connection getConnection() throws SQLException {
        System.out.println("创建新的连接！");
        return DriverManager.getConnection("jdbc:mysql://47.100.174.176:3306/study",
                "root",
                "@wangzehui123");
    }
}