package com.nova.mq.rabbit.listener;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.mq.rabbit.config.RabbitConstants;
import com.nova.mq.rabbit.entity.CanalCloudBean;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: wzh
 * @description: canal-cloud监听
 * @date: 2025/10/31 17:48
 */
@Slf4j(topic = "CanalCloudListener")
@Component
public class CanalCloudListener {

    /**
     *
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.CLOUD_CANAL_QUEUE))
    public void one(Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(tag, false);

        String json = new String(message.getBody());
        log.info("canal，消息id：{}，消息内容：{}", tag, json);

        CanalCloudBean canalCloudBean = JSONObject.parseObject(json, CanalCloudBean.class);
        log.info("canalCloudBean：{}", JSONUtil.toJsonStr(canalCloudBean));
    }
}
