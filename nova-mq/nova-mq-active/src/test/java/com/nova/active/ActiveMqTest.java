package com.nova.active;

import com.nova.common.constant.Destination;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/7 14:47
 */
@SpringBootTest
public class ActiveMqTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * activeMq测试
     */
    @Test
    public void activeMqTest() {
        Map<String, String> params = new HashMap<>(16);
        params.put("userId", "wzhTest");
        jmsTemplate.convertAndSend(Destination.TEST_DESTINATION, params);
    }
}
