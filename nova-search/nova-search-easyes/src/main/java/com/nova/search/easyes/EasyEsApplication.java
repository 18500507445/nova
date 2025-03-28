package com.nova.search.easyes;

import org.dromara.easyes.spring.annotation.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description: Easy-Es启动类
 * @date: 2023/07/13 13:53
 */
@EsMapperScan(value = "com.nova.search.easyes")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class EasyEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyEsApplication.class, args);
    }

}