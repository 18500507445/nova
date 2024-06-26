package com.nova.shopping.pay.web.dto;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.ObjectUtil;
import com.nova.shopping.common.constant.dto.PayReqDTO;
import com.nova.shopping.common.enums.PayWayEnum;
import lombok.*;

import java.nio.charset.StandardCharsets;

/**
 * @description: 支付请求公共参数类
 * @author: wzh
 * @date: 2023/4/14 19:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayParam extends PayReqDTO {

    private static final long serialVersionUID = -8461630040383707935L;

    /**
     * 支付配置id
     */
    private Long payConfigId;

    /**
     * 支付类型:1H5支付,2小程序,3app支付 4 jsapi微信原生 5ios沙盒 6钱包
     * 7快捷(银行卡) 8球币兑换 9微信(四方支付) 10支付宝(四方支付) 11扫码(微信、支付宝)
     */
    private String type;

    /**
     * 支付方式: {@link PayWayEnum}
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
    @Builder.Default
    private String subject = "充值";

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 交易状态
     */
    private Integer tradeStatus;

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
    private String appletUserId;

    /**
     * 语言类型
     * zh_CN, zh_TW, en
     */
    private String lang;

    /**
     * 产品id
     */
    private String productId;

    /**
     * 货币种类
     * CNY：人民币
     * USD：美元
     * HKD：港币
     * JPY：日元
     * GBP：英镑
     * EUR：欧元
     */
    @Builder.Default
    private String currencyType = "CNY";

    public String getReturnUrl() {
        if (ObjectUtil.isNotNull(returnUrl)) {
            returnUrl = URLDecoder.decode(returnUrl, StandardCharsets.UTF_8);
        }
        return returnUrl;
    }

}
