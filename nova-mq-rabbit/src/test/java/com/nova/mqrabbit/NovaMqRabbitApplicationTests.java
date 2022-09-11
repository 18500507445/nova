package com.nova.mqrabbit;

import com.nova.common.constant.Destination;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class NovaMqRabbitApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMsg() {
        Map<String, String> params = new HashMap<>(16);
        params.put("userId", "wzhTest");
        rabbitTemplate.convertAndSend(Destination.TEST_DESTINATION, params);
    }
}
