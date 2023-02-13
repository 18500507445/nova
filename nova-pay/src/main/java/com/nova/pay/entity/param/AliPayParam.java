package com.nova.pay.entity.param;

import lombok.*;

import java.io.Serializable;

/**
 * @description: 支付宝支付实体类
 * @author: wzh
 * @date: 2022/3/17 13:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AliPayParam implements Serializable {

    private static final long serialVersionUID = 6177572682414811592L;

    private String appId;

    /**
     * 支付宝公钥
     */
    private String publicKey;

    /**
     * 支付宝应用私钥
     */
    private String privateKey;

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
     * 通知url
     */
    private String notifyUrl;

    /**
     * 授权码
     */
    private String authCode;

    /**
     * 支付宝小程序用户id
     */
    private String userId;

    /**
     * 原因
     */
    private String reason;


}
