package com.nova.mq.kafka;

import cn.hutool.json.JSONUtil;
import com.nova.common.constant.Destination;
import com.nova.mq.kafka.config.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/7 14:47
 */
@SpringBootTest
@Slf4j
public class KafkaMqTest {

    @Resource
    private KafkaService kafkaService;

    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Test
    public void demoA() {
        kafkaTemplate.send("topicA", "123");
    }


    /**
     * kafka测试
     */
    @Test
    public void test() {
        Map<String, String> params = new HashMap<>(16);
        params.put("userId", "wzhTest");

        kafkaService.sendMessage(Destination.TEST_DESTINATION, JSONUtil.toJsonStr(params), new ListenableFutureCallback<SendResult<String, Object>>() {
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
            public void onFailure(@NotNull Throwable throwable) {
                log.error("发送消息失败！errMsg:{}", throwable.getCause(), throwable);
            }
        });
    }
}
