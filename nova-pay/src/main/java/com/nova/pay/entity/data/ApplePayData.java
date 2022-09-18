package com.nova.pay.entity.data;

import lombok.Data;

/**
 * @Description: 苹果支付实体类
 * @Author: wangzehui
 * @Date: 2022/6/18 13:37
 */
@Data
public class ApplePayData {

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 金额
     */
    private String totalAmount;

}
