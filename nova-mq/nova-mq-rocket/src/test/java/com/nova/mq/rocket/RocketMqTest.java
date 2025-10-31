package com.nova.mq.rocket;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description: rocket-mq测试类
 * @date: 2025/10/31 15:24
 */
@SpringBootTest
public class RocketMqTest {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void demoA() {
        for (int i = 0; i < 5; i++) {
            rocketMQTemplate.convertAndSend("nova-topic", "Hello,RocketMQ");
        }
    }

}
