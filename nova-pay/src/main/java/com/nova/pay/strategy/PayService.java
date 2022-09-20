package com.nova.pay.strategy;

import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;

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
     * 公共支付
     *
     * @param payParam
     * @return
     */
    AjaxResult pay(PayParam payParam);
}
