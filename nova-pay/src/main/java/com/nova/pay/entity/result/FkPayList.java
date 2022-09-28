package com.nova.pay.entity.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 支付列表
 * @Author: wangzehui
 * @Date: 2022/8/22 13:22
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FkPayList implements Serializable {

    private static final long serialVersionUID = -6964951482984262187L;

    /**
     * id
     */
    private Integer id;

    /**
     * source
     */
    private String source;

    /**
     * sid
     */
    private String sid;

    /**
     * 支付宝h5 0关闭 1显示
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
     * 苹果支付 0关闭 1显示
     */
    private Integer apple;

    /**
     * 易宝快捷 0关闭 1显示
     */
    private Integer yeePayQuick;

    /**
     * 易宝钱包 0关闭 1显示
     */
    private Integer yeePayWallet;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 阿里logo
     */
    private String aliLogoUrl;

    /**
     * 微信logo
     */
    private String wechatLogoUrl;

    /**
     * 苹果logo
     */
    private String appleLogoUrl;

    /**
     * 易宝logo
     */
    private String yeePayLogoUrl;

    /**
     * 球币兑换
     */
    private Integer ballCoin;

    /**
     * 球币兑换logo
     */
    private String ballCoinLogoUrl;


}
