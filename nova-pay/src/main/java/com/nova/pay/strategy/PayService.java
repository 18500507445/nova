package com.nova.pay.strategy;

import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.enums.PayWayEnum;

/**
 * @Description: 支付策略接口service
 * @Author: wangzehui
 * @Date: 2022/9/18 11:28
 */
public interface PayService {

    /**
     * 获取支付类型
     *
     * @return
     */
    PayWayEnum getPayType();

    /**
     * 支付
     *
     * @param payParam
     * @return
     */
    AjaxResult pay(PayParam payParam);

    /**
     * 退款
     *
     * @param payParam
     * @return
     */
    AjaxResult refund(PayParam payParam);

    /**
     * 查询订单
     *
     * @param payParam
     * @return
     */
    AjaxResult queryOrder(PayParam payParam);

    /**
     * 获取openId
     *
     * @param payParam
     * @return
     */
    AjaxResult getOpenId(PayParam payParam);

    /**
     * 关单
     *
     * @param payParam
     * @return
     */
    AjaxResult closeOrder(PayParam payParam);

    /**
     * 商家转账
     * 例如：微信商户号转给个人
     *
     * @param payParam
     * @return
     */
    AjaxResult merchantTransfer(PayParam payParam);
}
