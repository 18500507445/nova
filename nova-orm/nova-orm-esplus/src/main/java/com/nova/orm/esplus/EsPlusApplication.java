package com.nova.orm.esplus;

import org.dromara.easyes.spring.annotation.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description: es-plus启动类
 * @date: 2025/10/30 16:39
 */
@EsMapperScan(value = "com.nova.orm.esplus")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class EsPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsPlusApplication.class, args);
    }
}
