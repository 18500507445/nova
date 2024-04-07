package com.nova.shopping.pay.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单表明细表(MyPayOrder)实体类
 *
 * @author wzh
 * @since 2023-04-14 19:27:15
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PayOrder implements Serializable {

    private static final long serialVersionUID = -21899224097992665L;


    private Long id;


    private String source;

    /**
     * 渠道号
     */
    private String sid;

    /**
     * 支付配置表id
     */
    private Long payConfigId;

    /**
     * 产品id
     */
    private String productId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 订单id、聚合订单id(nt_pay_master_order)
     */
    private String orderId;

    /**
     * 交易订单号 支付宝同字段、微信和苹果：transaction_id
     */
    private String tradeNo;

    /**
     * 0支付中，1成功，2失败，3退款， 4处理中，5关单
     */
    private Integer tradeStatus;

    /**
     * 金额
     */
    private BigDecimal fee;

    /**
     * 1h5 2小程序 3app 4微信原生jsapi 5沙盒 6钱包 7快捷 8球币兑换 9微信(四方支付) 10支付宝(四方支付) 11扫码(微信、支付宝)
     */
    private Integer type;

    /**
     * 1支付宝 2微信 3苹果 4yeePay 5谷歌 6快手 7华为支付 99金币兑换
     */
    private Integer payWay;

    /**
     * 货币种类   CNY：人民币,USD：美元,HKD：港币,JPY：日元,GBP：英镑,EUR：欧元
     */
    private String currencyType;

    /**
     * 苹果支付 sign、易宝支付token、快手的小程序平台订单号
     */
    private String sign;

    /**
     * 充当开通业务code，例如支付成功通知后，发送mq自动处理
     */
    private Integer businessCode;

    /**
     * 备注
     * 1.易宝1级收款商户
     * 2.易宝2级默认收款方式 银行卡、钱包
     * 3.苹果支付验签结果
     * 4.快手是否结算
     */
    private String remark;

    /**
     * 操作人
     * 备注：快手存入用户openId
     */
    private String operator;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * sql查询用 !=remark字段
     */
    private String notEqualRemark;

}

