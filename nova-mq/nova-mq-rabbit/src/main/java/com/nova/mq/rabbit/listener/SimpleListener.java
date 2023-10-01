package com.nova.mq.rabbit.listener;

import cn.hutool.json.JSONUtil;
import com.nova.common.core.model.business.MessageBO;
import com.nova.mq.rabbit.config.RabbitConfig;
import com.nova.mq.rabbit.config.RabbitConstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 简单分发，绑定到默认的交换机上(amqp-default)
 * @link <a href="https://img-blog.csdnimg.cn/15dc5ce2c06548cd8f61154496134b5a.png#pic_center">默认交换机</a>
 * 默认交换机绑定到每个队列，路由key等于队列名称。无法显式绑定到默认交换或从默认交换解除绑定。它也不能被删除。
 * @author: wzh
 * @date: 2023/1/6 15:42
 */
@Component
public class SimpleListener {

    /**
     * 简单队列1 (无返回值)
     * RabbitListener(queues = RabbitConstants.QUEUE_SIMPLE_ONE) 用来绑定队列
     * {@link RabbitConfig#queueSimpleOne()} 需要申明队列
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_SIMPLE_ONE)
    public void one(Message message) {
        System.err.println("简单模式one消息：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 简单队列2 (有返回值)
     * {@link RabbitConfig#queueSimpleTwo()}} 需要申明队列
     * @param message
     * @return
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_SIMPLE_TWO)
    public String two(Message message) {
        System.err.println("简单模式two消息：" + JSONUtil.toJsonStr(new String(message.getBody())));
        return "收到";
    }

    /**
     * 简单队列3 (无序申明队列，直接注解搞定)
     * queuesToDeclare属性：如果没有就创建队列 new Queue，否则不创建，和@Queue搭配使用
     *
     * @param message
     * @return
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_SIMPLE_THREE))
    public void three(Message message) {
        System.err.println("简单模式three消息：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 简单队列4 实体类接收参数
     *
     * @param message
     * @return
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_SIMPLE_FOUR))
    public void four(MessageBO message) {
        System.err.println("简单模式four消息：" + JSONUtil.toJsonStr(message));
    }

    /**
     * 简单队列5 map接收参数
     *
     * @param msg
     * @return
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_SIMPLE_FIVE))
    public void five(Map<String, Object> msg) {
        System.err.println("简单模式five消息：" + JSONUtil.toJsonStr(msg));
    }


}
