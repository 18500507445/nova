package com.nova.mq.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/21 09:50
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableKafka
public class KafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }

}
