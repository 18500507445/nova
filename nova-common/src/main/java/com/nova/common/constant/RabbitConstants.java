package com.nova.common.constant;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/5 12:49
 */
public class RabbitConstants {

    /**
     * 直连交换机(自带)
     */
    public static final String EXCHANGE_DIRECT = "amq.direct";

    /**
     * 死信交换机
     */
    public static final String EXCHANGE_DIRECT_DLX = "dlx.direct";

    /**
     * 广播交换机(自带)
     */
    public static final String EXCHANGE_FANOUT = "amq.fanout";

    /**
     * 简单模式队列
     */
    public static final String QUEUE_SIMPLE_ONE = "queue-simple-one";
    public static final String QUEUE_SIMPLE_TWO = "queue-simple-two";
    public static final String QUEUE_SIMPLE_THREE = "queue-simple-three";
    public static final String QUEUE_SIMPLE_FOUR = "queue-simple-four";
    public static final String QUEUE_SIMPLE_FIVE = "queue-simple-five";

    /**
     * 工作模式队列
     */
    public static final String QUEUE_WORK_ONE = "queue-work-one";
    public static final String QUEUE_WORK_TWO = "queue-work-two";
    public static final String QUEUE_WORK_THREE = "queue-work-three";


    /**
     * 广播模式队列
     */
    public static final String QUEUE_FANOUT_EMAIL = "queue-fanout-email";
    public static final String QUEUE_FANOUT_SMS = "queue-fanout-sms";

    /**
     * 直连模式队列
     */
    public static final String QUEUE_DIRECT_ONE = "queue-direct-one";
    public static final String QUEUE_DIRECT_TWO = "queue-direct-two";
    public static final String QUEUE_DIRECT_DLX = "queue-direct-dlx";

}
