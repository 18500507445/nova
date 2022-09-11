package com.nova.mqactive.listener;

import com.nova.common.constant.Destination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;

/**
 * @Description: 测试active-mq监听器
 * @Author: wangzehui
 * @Date: 2022/9/11 09:01
 */
@Component
public class ActiveMqListener {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMqListener.class);

    @JmsListener(destination = Destination.TEST_DESTINATION)
    public void testActiveMqListener(Message msg) {
        MapMessage obj = (MapMessage) msg;
        try {
            String userId = obj.getString("userId");
            System.out.println(userId);
        } catch (Exception e) {
            logger.error("testActiveMqListener异常:{}" + e.getMessage());
        }
    }
}
