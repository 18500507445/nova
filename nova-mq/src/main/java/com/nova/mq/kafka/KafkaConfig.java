package com.nova.mq.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: kafka配置
 * @Author: wangzehui
 * @Date: 2022/12/20 21:29
 */
@Slf4j
@Configuration
public class KafkaConfig {

    @Bean
    public KafkaProducerUtil kafkaProducerUtil() {
        return new KafkaProducerUtil();
    }
}
