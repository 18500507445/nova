package com.nova.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: wzh
 * @date: 2022/12/29 20:31
 */
@EnableAspectJAutoProxy
@Configuration
@ComponentScan("com.nova.spring.config")
@Import({MysqlConfiguration.class, Date.class})
public class TestConfiguration {

    @Resource
    private Connection connection;

    @Resource
    private Date date;

    @PostConstruct
    public void init() {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        System.out.println(format + " -> " + connection);
    }
}

