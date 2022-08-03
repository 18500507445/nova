package com.nova.oldPay.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 支付宝实体类
 * @Author: wangzehui
 * @Date: 2022/6/18 13:13
 */
@Data
public class AliPayData implements Serializable {

    private static final long serialVersionUID = 6177572682414811592L;

    public static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * 商品的标题
     */
    private String subject;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 金额
     */
    private String totalAmount;

    /**
     * 对一笔交易的具体描述信息
     */
    private String body;

    /**
     * 支付成功后回跳地址,H5支付特有
     */
    private String returnUrl = "http://backtoclient.com";

    /**
     * 授权码
     */
    private String authCode;

    /**
     * 支付宝小程序用户id
     */
    private String userId;

    private String appId;

    private String publicKey;

    private String privateKey;

    public AliPayData() {

    }

    public AliPayData(String subject, String outTradeNo, String totalAmount, String body, String returnUrl, String userId, String appId) {
        this.subject = subject;
        this.outTradeNo = outTradeNo;
        this.totalAmount = totalAmount;
        this.body = body;
        this.returnUrl = returnUrl;
        this.userId = userId;
        this.appId = appId;
    }

}
