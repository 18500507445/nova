package com.nova.pay.entity.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 支付配置对象 fk_pay_config
 * @author: wangzehui
 * @date: 2022/8/22 15:37
 */
@Data
public class FkPayConfig implements Serializable {

    private static final long serialVersionUID = -3361189197088323155L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 1支付宝 2微信 3苹果 4易宝
     */
    private Integer payWay;

    /**
     * 体育红单的支付类型 对应：uc_paytype_info表
     */
    private String payType;

    /**
     * 微信/支付宝应用id、易宝appKey
     */
    private String appId;

    /**
     * 微信应用秘钥
     */
    private String appSecret;

    /**
     * 微信商户号/易宝商户编号
     */
    private String mchId;

    /**
     * 微信支付秘钥
     */
    private String paySecret;

    /**
     * 证书号
     */
    private String serialNo;

    /**
     * 商户支付v3key，有值微信v3支付，空就是v2支付
     */
    private String apiV3Key;

    /**
     * 支付宝公钥/易宝公钥
     */
    private String publicKey;

    /**
     * 支付宝应用私钥/易宝平台私钥
     */
    private String privateKey;

    /**
     * 微信p12证书地址/支付宝appCertPublicKey.crt
     */
    private String keyPath;

    /**
     * 微信v3支付apiclient_key.pem证书地址/支付包alipayCertPublicKey_RSA2.crt证书地址
     */
    private String privateKeyPath;

    /**
     * 微信v3支付apiclient_cert.pem证书地址/支付宝alipayRootCert.crt证书地址
     */

    private String privateCertPath;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 是否生效  0不生效 1生效  -1 作废
     */
    private Integer status;

    /**
     * $column.columnComment
     */
    private String source;

    /**
     * 渠道号
     */
    private String sid;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 备注
     * 1.易宝1级收款商户
     * 2.易宝2级默认收款方式 银行卡、钱包
     * 3.苹果支付验签
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
