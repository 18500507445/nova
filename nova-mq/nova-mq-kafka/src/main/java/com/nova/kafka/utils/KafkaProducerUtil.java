package com.nova.kafka.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * @description: kafka生产者工具类
 * @author: wzh
 * @date: 2022/12/20 21:29
 */
@Slf4j
public class KafkaProducerUtil {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 放开kafka模版类，便于后续扩展
     */
    public KafkaTemplate<String, Object> getKafkaTemplate() {
        return kafkaTemplate;
    }

    /**
     * 向指定topic发送消息
     *
     * @param topicName 主题名
     * @param data      消息(json格式)
     */
    public void sendMessage(String topicName, Object data) {
        kafkaTemplate.send(topicName, data);
    }

    /**
     * 向指定topic、指定key发送消息
     *
     * @param topicName 主题名
     * @param key       消息key
     * @param data      消息(json格式)
     */
    public void sendMessage(String topicName, String key, Object data) {
        kafkaTemplate.send(topicName, key, data);
    }

    /**
     * 向指定topic、指定分区、指定key发送消息
     *
     * @param topicName 主题名
     * @param partition 分区
     * @param key       消息key
     * @param data      消息(json格式)
     */
    public void sendMessage(String topicName, Integer partition, String key, Object data) {
        kafkaTemplate.send(topicName, partition, key, data);
    }

    /**
     * 向指定topic发送消息(带回调)
     *
     * @param topicName                主题名
     * @param data                     消息(json格式)
     * @param listenableFutureCallback 回调方法(包括成功或失败)
     */
    public <T> void sendMessage(String topicName, String data, ListenableFutureCallback<? super SendResult<String, Object>>
            listenableFutureCallback) {
        kafkaTemplate.send(topicName, data).addCallback(listenableFutureCallback);
    }

    /**
     * 向指定topic发送消息(带回调)
     *
     * @param topicName                主题名
     * @param key                      消息key
     * @param data                     消息(json格式)
     * @param listenableFutureCallback 回调方法(包括成功或失败)
     */
    public <T> void sendMessage(String topicName, String key, String data, ListenableFutureCallback<? super SendResult<String, Object>>
            listenableFutureCallback) {
        kafkaTemplate.send(topicName, key, data).addCallback(listenableFutureCallback);
    }

    /**
     * 向指定topic发送消息(带回调)
     *
     * @param topicName                主题名
     * @param partition                分区
     * @param key                      消息key
     * @param data                     消息(json格式)
     * @param listenableFutureCallback 回调方法(包括成功或失败)
     */
    public <T> void sendMessage(String topicName, Integer partition, String key, String data, ListenableFutureCallback<? super SendResult<String, Object>>
            listenableFutureCallback) {
        kafkaTemplate.send(topicName, partition, key, data).addCallback(listenableFutureCallback);
    }

}
