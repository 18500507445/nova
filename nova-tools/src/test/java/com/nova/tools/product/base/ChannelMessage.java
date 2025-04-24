package com.nova.tools.product.base;

/**
 * @author: wzh
 * @description: 渠道消息接口，（1）主动拉取消息推送接口 如：京东（2）渠道回调通知消息 如：网易严选
 * @date: 2023/10/31 14:29
 */
public interface ChannelMessage {

    /**
     * 商品消息（基础信息的消息、库存消息、价格消息、上下架）
     */
    <T> void productMessage(T t);

    /**
     * 订单消息（取消、异常）
     */
    <T> void orderMessage(T t);

    /**
     * 物流消息（退货、收货）
     */
    <T> void logisticsMessage(T t);

    /**
     * 售后消息
     */
    <T> void afterSaleMessage(T t);

}
