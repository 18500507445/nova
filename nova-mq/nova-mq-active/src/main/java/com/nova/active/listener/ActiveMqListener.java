package com.nova.active.listener;

import com.nova.common.constant.Destination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;

/**
 * @description: 测试active-mq监听器
 * @author: wzh
 * @date: 2022/9/11 09:01
 */
@Slf4j
@Component
public class ActiveMqListener {

    @JmsListener(destination = Destination.TEST_DESTINATION)
    public void testActiveMqListener(Message msg) {
        MapMessage obj = (MapMessage) msg;
        try {
            String userId = obj.getString("userId");
            System.err.println(userId);
        } catch (Exception e) {
            log.error("testActiveMqListener异常:{}" + e.getMessage());
        }
    }
}
