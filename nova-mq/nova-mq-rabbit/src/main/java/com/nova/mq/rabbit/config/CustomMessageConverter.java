package com.nova.mq.rabbit.config;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSON;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description 自定义消息转换器
 * @date: 2023/11/30 11:18
 */
@Component
public class CustomMessageConverter implements MessageConverter {

    /**
     * 消息转换
     * 通过fastJson的parse方法进行分别处理，防止传json再转一次json导致message.body获取内容解析报错
     *
     * @param o                 the object to convert
     * @param messageProperties The message properties.
     * @return
     * @throws MessageConversionException
     */
    @NotNull
    @Override
    public Message toMessage(@NotNull Object o, @NotNull MessageProperties messageProperties) throws MessageConversionException {
        try {
            Object parse = JSON.parse(Convert.toStr(o));
            return new Message(JSON.toJSONBytes(parse), messageProperties);
        } catch (Exception e) {
            return new Message(JSON.toJSONBytes(o));
        }
    }

    /**
     * 根据消息类型，解析成<T>，根据message.getMessageProperties().getInferredArgumentType()进行类型推断 </>
     * 支持的有（1）jsonString转Message对象（2）对象转对象
     * @param message the message to convert
     * @return
     * @throws MessageConversionException
     */
    @NotNull
    @Override
    public Object fromMessage(@NotNull Message message) throws MessageConversionException {
        return JSON.parseObject(message.getBody(), message.getMessageProperties().getInferredArgumentType());
    }
}
