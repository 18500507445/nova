package com.nova.mq.kafka.listener;

import com.nova.common.constant.Destination;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * @description: Kafka消费者监听
 * @author: wzh
 * @date: 2022/12/20 21:27
 */
@Slf4j
@Component
public class KafkaConsumerListener {

    /**
     * topics topic名称
     * groupId 是设置消费组，
     * 默认情况下 项目启动后，没有这个 topic 的话，程序就会自动创建
     * <p>
     * KafkaListener注解 常用的几个属性说明
     * <ul>
     * <li>topics 监听的 Topic 数组 (支持 Spel)</li>
     * <li>topicPattern 监听的 Topic 表达式 (支持 Spel)</li>
     * <li>groupId 消费者分组</li>
     * <li>errorHandler 使用消费异常处理器 KafkaListenerErrorHandler 的 Bean 名字</li>
     * <li>concurrency 自定义消费者监听器的并发数</li>
     * <li>autoStartup 是否自动启动监听器。默认情况下，为 true 自动启动。</li>
     * <li>properties Kafka Consumer 拓展属性</li>
     * </ul>
     */
    @KafkaListener(topics = "topicA", groupId = "consumer-group-01-" + "topicA")
    public void onMessage(Object message) {
        log.info("[KafkaConsumer01 consumer-group-01][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    @KafkaListener(groupId = "nova-kafka-consumer", topics = {Destination.TEST_DESTINATION})
    public void onMessage(ConsumerRecord<Integer, String> record) {
        log.info("kafka消息订阅！topic:{}，partition:{}，value:{}", record.topic(), record.partition(), record.value());
    }

}
