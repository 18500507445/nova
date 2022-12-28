package com.nova.pay.service.fk;

import java.util.Map;

/**
 * @description: 订单接口
 * @author: wangzehui
 * @date: 2022/8/24 14:30
 */
public interface FkOrderService {

    /**
     * 获取orderId
     *
     * @param map
     * @return
     */
    String getOrderId(Map<String, String> map);

    /**
     * 通用请求方法
     *
     * @param map
     * @return
     */
    Map<String, String> getCommon(Map<String, String> map);

    /**
     * 成功订单处理
     *
     * @param source
     * @param sid
     * @param businessCode
     * @param orderId
     * @param userName
     * @param tradeStatus
     * @param amount
     * @param payType
     */
    void successOrderHandler(String source, String sid, int businessCode, String orderId, String userName, String tradeStatus, String amount, String payType);

}
