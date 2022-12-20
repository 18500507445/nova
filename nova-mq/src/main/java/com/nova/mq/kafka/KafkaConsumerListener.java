package com.nova.mq.kafka;

import com.nova.common.constant.Destination;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * @Description: Kafka消费者监听
 * @Author: wangzehui
 * @Date: 2022/12/20 21:27
 */
@Slf4j
@Component
public class KafkaConsumerListener {

    @KafkaListener(groupId = "nova-kafka-consumer", topics = {Destination.TEST_DESTINATION})
    public void onMessage(ConsumerRecord<Integer, String> record) {
        log.info("kafka消息订阅！topic:{},partition:{},value:{}", record.topic(), record.partition(), record.value());
    }

}
