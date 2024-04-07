package com.nova.shopping.pay.repository.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付配置表(MyPayConfig)实体类
 *
 * @author wzh
 * @since 2023-04-14 19:27:14
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PayConfig implements Serializable {

    private static final long serialVersionUID = 840611172298917851L;


    private Long id;


    private String source;

    /**
     * 渠道号
     */
    private String sid;

    /**
     * 1支付宝 2微信 3苹果 4易宝 5谷歌支付 6快手支付 7华为支付 99兑换
     */
    private Integer payWay;

    /**
     * 微信appId/支付宝appId/易宝appKey/华为client_id
     */
    private String appId;

    /**
     * 微信应用秘钥/华为client_secret
     */
    private String appSecret;

    /**
     * 微信商户号/易宝商户编号/谷歌应用名称
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
     * 微信p12证书地址/支付宝appCertPublicKey.crt/谷歌p12证书
     */
    private String keyPath;

    /**
     * 微信v3支付apiclient_key.pem证书地址/支付宝alipayCertPublicKey_RSA2.crt证书地址
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
     * 备注
     */
    private String remark;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}

