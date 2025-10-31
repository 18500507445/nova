package com.nova.mq.rocket.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description: rocket监听
 * @date: 2025/10/31 15:27
 */
@Slf4j(topic = "RocketListener")
@Component
@RocketMQMessageListener(topic = "nova-topic", consumerGroup = "rocket-mq-consumer")
public class RocketListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        // 处理消息的逻辑
        log.info("Received message: {}", s);
    }
}
