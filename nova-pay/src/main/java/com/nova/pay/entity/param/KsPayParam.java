package com.nova.pay.entity.param;

import lombok.*;

/**
 * @description: 快手支付请求实体类
 * @author: wzh
 * @date: 2022/11/15 14:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KsPayParam {

    /**
     * 小程序的 app_id
     */
    private String appId;

    /**
     * 小程序的密钥
     */
    private String appSecret;

    /**
     * 拥有小程序支付权限的accessToken
     *
     * @see <a href="https://mp.kuaishou.com/docs/develop/server/getAccessToken.html">获取凭证</a>
     */
    private String accessToken;

    /**
     * 支付类型
     * 9微信(四方支付) 10支付宝(四方支付)
     */
    private int payType;

    /**
     * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一
     * 示例值：1217752501201407033233368018
     */
    private String outOrderNo;

    /**
     * 开发者的结算单号，小程序唯一。
     */
    private String outSettleNo;

    /**
     * 快手用户在当前小程序的open_id，可通过
     *
     * @see <a href="https://mp.kuaishou.com/docs/develop/api-next/open/login/ks.login.html">操作获取</a>
     */
    private String openId;

    /**
     * 用户支付金额，单位为[分]。不允许传非整数的数值。
     */
    private Long totalAmount;

    /**
     * 商品描述/商品详情。注：1汉字=2字符。
     */
    private String subject;

    /**
     * 开发者对核心字段签名, 签名方式见 附录
     */
    private String sign;

    /**
     * 通知URL必须为直接可访问的URL，不允许携带查询串
     */
    private String notifyUrl;

    /**
     * 下单商品id，需与商品对接 (opens new window)时的product_id一致，长度限制256个英文字符，1个汉字=2个英文字符；
     */
    private String goodsId;

    /**
     * 订单详情页跳转path。长度限制500个英文字符，1个汉字=2个英文字符；
     * 示例值：/page/index/anima
     */
    private String goodsDetailUrl;

    /**
     * 单商品购买多份场景，示例值：[{"copies":2}]， 内容见multi_copies_goods_info字段说明：购买份数
     */
    private String multiCopiesGoodsInfo;

    /**
     * 无收银台版本预下单场景，json格式，示例如下：
     * {"provider": "ALIPAY","provider_channel_type":"NORMAL"}
     * 注意：
     * 1、provider：支付方式，枚举值，目前只支持"WECHAT"、"ALIPAY"两种
     * 2、provider_channel_type：支付方式子类型，枚举值，目前只支持"NORMAL"
     */
    private String provider;

    /**
     * 图片url
     */
    private String url;


}
