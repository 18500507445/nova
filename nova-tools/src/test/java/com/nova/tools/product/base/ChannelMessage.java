package com.nova.tools.product.base;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wzh
 * @description 渠道消息接口，（1）主动拉取消息推送接口 如：京东（2）渠道回调通知消息 如：网易严选
 * @date: 2023/10/31 14:29
 */
public interface ChannelMessage {

    /**
     * 商品变更
     */
    String productChange(HttpServletRequest request);

    /**
     * 商品价格变更
     */
    String priceChange(HttpServletRequest request);

    /**
     * 订单状态变更通知
     */
    String orderStatusChange(HttpServletRequest request);

    /**
     * 订单发货信息变更通知
     */
    String orderDeliveryChange(HttpServletRequest request);

    /**
     * 售后单状态变更通知
     */
    String afterSaleStatusChange(HttpServletRequest request);

    /**
     * 渠道消息池通知接口
     */
    String messageChange();

}
