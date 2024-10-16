package com.nova.mq.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: kafka配置
 * @author: wzh
 * @date: 2022/12/20 21:29
 */
@Slf4j
@Configuration
public class KafkaConfig {

    @Bean(name = "kafkaService")
    public KafkaService kafkaProducerUtil() {
        return new KafkaService();
    }
}
