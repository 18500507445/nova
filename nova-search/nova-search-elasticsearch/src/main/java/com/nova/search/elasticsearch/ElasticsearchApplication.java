package com.nova.search.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author: wzh
 * @description: ES启动类
 * @date: 2023/07/13 13:53
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//ES包扫描
@EnableElasticsearchRepositories(basePackages = "com.nova.search.elasticsearch.repository")
public class ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }
}