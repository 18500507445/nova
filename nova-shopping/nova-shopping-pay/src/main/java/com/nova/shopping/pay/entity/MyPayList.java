package com.nova.shopping.pay.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付列表(MyPayList)实体类
 *
 * @author wzh
 * @since 2023-04-14 19:27:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MyPayList implements Serializable {

    private static final long serialVersionUID = -16195139042877361L;


    private Long id;


    private String source;

    /**
     * 渠道号
     */
    private String sid;

    /**
     * 支付宝h5 0关闭 1显示 备注：支付宝h5和app都走h5支付
     */
    private Integer aliH5;

    /**
     * 支付宝app 0关闭 1显示
     */
    private Integer aliApp;

    /**
     * 支付宝小程序 0关闭 1显示
     */
    private Integer aliApplet;

    /**
     * 支付宝logo
     */
    private String aliLogoUrl;

    /**
     * 微信h5 0关闭 1显示
     */
    private Integer wechatH5;

    /**
     * 微信客户端 0关闭 1显示
     */
    private Integer wechatApp;

    /**
     * 微信内原生、小程序 0关闭 1显示
     */
    private Integer wechatJsapi;

    /**
     * 微信logo
     */
    private String wechatLogoUrl;

    /**
     * 苹果支付 0关闭 1显示
     */
    private Integer apple;

    /**
     * 苹果logo
     */
    private String appleLogoUrl;

    /**
     * 易宝快捷 0关闭 1显示
     */
    private Integer yeePayQuick;

    /**
     * 易宝钱包 0关闭 1显示
     */
    private Integer yeePayWallet;

    /**
     * 易宝logo
     */
    private String yeePayLogoUrl;

    /**
     * 谷歌支付
     */
    private Integer googlePay;

    /**
     * 谷歌支付logo
     */
    private String googleLogoUrl;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}

