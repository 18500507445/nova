package com.nova.pay.entity.param;

import lombok.*;

import java.io.Serializable;

/**
 * @description:
 * @author: wzh
 * @date: 2022/7/1 17:32
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class YeePayParam implements Serializable {

    private static final long serialVersionUID = 105489645203813170L;


    private String appKey;

    private String publicKey;

    private String privateKey;

    /**
     * 平台商户编号
     */
    private String parentMerchantNo;

    /**
     * 用户收款商户编号
     */
    private String merchantNo;

    /**
     * 支付类型 6钱包 7快捷支付
     */
    private Integer payType;

    /**
     * 收款方式0无 1钱包 2银行卡
     */
    private String collectionStatus;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 银行卡
     */
    private String bankCard;

    /**
     * 银行卡编码
     */
    private String bankCardCode;

    /**
     * 省id
     */
    private String provinceId;

    /**
     * 市id
     */
    private String cityId;

    /**
     * 区县id
     */
    private String areaId;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 身份证正面url
     */
    private String idCardUrl;

    /**
     * 身份证背面url
     */
    private String idCardBackUrl;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 易宝钱包账户ID
     */
    private String walletId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 藏品名称
     */
    private String goodsName;

    /**
     * 藏品id
     */
    private String worksId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 入网请求号
     */
    private String requestNo;

    /**
     * 订单金额 0.02
     */
    private String amount;

    /**
     * 手续费 0.02
     */
    private String fee;

    /**
     * 唤起收银台token
     */
    private String token;

    /**
     * 钱包首页返回商户页面地址
     */
    private String returnUrl;

    /**
     * 支付通知地址
     */
    private String notifyUrl;

    /**
     * uniqueOrderNo:易宝收款订单号
     */
    private String tradeNo;

    private String startTime;

    private String endTime;

    public YeePayParam(String idCardUrl) {
        this.idCardUrl = idCardUrl;
    }


}
