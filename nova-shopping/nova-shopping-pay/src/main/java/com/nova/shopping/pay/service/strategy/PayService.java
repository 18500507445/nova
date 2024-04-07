package com.nova.shopping.pay.service.strategy;


import com.nova.shopping.common.constant.result.AjaxResult;
import com.nova.shopping.common.enums.PayWayEnum;
import com.nova.shopping.pay.entity.param.PayParam;

/**
 * @description: 支付接口
 * @author: wzh
 * @date: 2023/3/20 17:35
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
     * @param param
     * @return
     */
    AjaxResult pay(PayParam param);

    /**
     * 退款
     *
     * @param param
     * @return
     */
    AjaxResult refund(PayParam param);

    /**
     * 查询订单
     *
     * @param param
     * @return
     */
    AjaxResult queryOrder(PayParam param);

    /**
     * 获取openId
     *
     * @param param
     * @return
     */
    AjaxResult getOpenId(PayParam param);

    /**
     * 关单
     *
     * @param param
     * @return
     */
    AjaxResult closeOrder(PayParam param);

    /**
     * 商家转账
     * 例如：微信商户号转给个人
     *
     * @param param
     * @return
     */
    AjaxResult merchantTransfer(PayParam param);
}
