package com.nova.pay.entity.param;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.nio.charset.StandardCharsets;

/**
 * @Description: 支付请求公共参数类
 * @Author: wangzehui
 * @Date: 2022/8/23 14:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PayParam extends BaseParam {

    private static final long serialVersionUID = -8461630040383707935L;

    /**
     * 支付配置id
     */
    private Long payConfigId;

    /**
     * 支付类型:1H5支付,2小程序,3app支付 4 jsapi微信原生 5ios沙盒 6钱包 7快捷(银行卡) 8球币兑换
     */
    private String type;

    /**
     * 支付方式:1支付宝 2微信 3苹果 4易宝 5兑换
     */
    private Integer payWay;

    /**
     * 支付宝小程序支付 需要授权code来获取唯一标识userId
     * <p>
     * 微信小程序、原生微信内支付也需要授权code来换取openId
     */
    private String authCode;

    /**
     * 用户id
     */
    private String openId;

    /**
     * 支付宝：商品的标题
     * 微信：对一笔交易的具体描述信息
     */
    private String subject = "球场大咖";

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 交易状态
     */
    private Integer tradeStatus = 0;

    /**
     * 金额 单位元
     */
    private String totalAmount;

    /**
     * 支付成功后回跳地址,H5支付特有
     */
    private String returnUrl;

    /**
     * 授权后拿到的userId
     */
    private String appletUserId = "";

    /**
     * 语言类型
     * zh_CN, zh_TW, en
     */
    private String lang = "zh_CN";

    /**
     * 产品id
     */
    private String productId;

    public String getSubject() {
        if (ObjectUtil.isNotNull(subject)) {
            subject = URLDecoder.decode(subject, StandardCharsets.UTF_8);
        }
        return subject;
    }

    public String getReturnUrl() {
        if (ObjectUtil.isNotNull(returnUrl)) {
            returnUrl = URLDecoder.decode(returnUrl, StandardCharsets.UTF_8);
        }
        return returnUrl;
    }

}
