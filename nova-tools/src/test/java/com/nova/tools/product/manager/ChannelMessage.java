package com.nova.tools.product.manager;

import java.util.Map;

/**
 * @author: wzh
 * @description 渠道消息接口，（1）主动拉取消息推送接口 如：京东（2）渠道回调通知消息 如：网易严选
 * @date: 2023/10/31 14:29
 */
public interface ChannelMessage {

    /**
     * 商品变更
     */
    String productChange(Map<String, Object> params);

    /**
     * 商品价格变更
     */
    String priceChange(Map<String, Object> params);

    /**
     * 订单状态变更
     */
    String orderStatusChange(Map<String, Object> params);

    /**
     * 订单发货信息变更
     */
    String orderDeliveryChange(Map<String, Object> params);

    /**
     * 售后单状态变更
     */
    String afterSaleStatusChange(Map<String, Object> params);

    /**
     * 地址变更
     */
    String addressChange(Map<String, Object> params);


}
