package com.nova.mqactive;

import com.nova.common.constant.Destination;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class NovaMqActiveApplicationTests {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void testSendMsg() {
        Map<String, String> params = new HashMap<>(16);
        params.put("userId", "wzhTest");
        jmsTemplate.convertAndSend(Destination.TEST_DESTINATION, params);
    }
}
