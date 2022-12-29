package com.nova.mq;

import cn.hutool.json.JSONUtil;
import com.nova.common.constant.Destination;
import com.nova.mq.kafka.KafkaProducerUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
class MqApplicationTests {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private KafkaProducerUtil kafkaProducerUtil;

    /**
     * activeMq测试
     */
    @Test
    public void activeMqTest() {
        Map<String, String> params = new HashMap<>(16);
        params.put("userId", "wzhTest");
        jmsTemplate.convertAndSend(Destination.TEST_DESTINATION, params);
    }

    /**
     * rabbitMq测试
     */
    @Test
    public void rabbitMqTest() {
        Map<String, String> params = new HashMap<>(16);
        params.put("userId", "wzhTest");
        rabbitTemplate.convertAndSend(Destination.TEST_DESTINATION, params);
    }

    /**
     * kafka测试
     */
    @Test
    public void KafkaTest() {
        Map<String, String> params = new HashMap<>(16);
        params.put("userId", "wzhTest");

        kafkaProducerUtil.sendMessage(Destination.TEST_DESTINATION, JSONUtil.toJsonStr(params), new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> sendResult) {
                log.info("发送消息成功！topic:{}, partition:{}, offset:{}, key:{}, value:{}",
                        sendResult.getRecordMetadata().topic(),
                        sendResult.getRecordMetadata().partition(),
                        sendResult.getRecordMetadata().offset(),
                        sendResult.getProducerRecord().key(),
                        sendResult.getProducerRecord().value());
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("发送消息失败！errMsg:{}", throwable.getCause(), throwable);
            }
        });
    }
}
