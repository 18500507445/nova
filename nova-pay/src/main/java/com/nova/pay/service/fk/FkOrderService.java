package com.nova.pay.service.fk;

import java.util.Map;

/**
 * @Description: 订单接口
 * @Author: wangzehui
 * @Date: 2022/8/24 14:30
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
     * 充值回调后加款
     *
     * @param orderId
     * @param userName
     * @param tradeStatus -1失败 1成功
     * @param amount      单位元
     * @param payType
     * @return
     */
    void recharge(String orderId, String userName, String tradeStatus, String amount, String payType);

    /**
     * 开通尊享会员
     */
    String openExclusiveVip(String userName, String fee);
}
