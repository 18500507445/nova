package com.nova.pay.entity.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: wzh
 * @date: 2022/8/22 15:45
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FkPayOrder implements Serializable {

    private static final long serialVersionUID = 1512889166096648524L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 支付配置id
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
     * 0默认，1成功，2失败，3退款, 4处理中
     */
    private Integer tradeStatus;

    /**
     * 价格 单位元
     */
    private BigDecimal fee;

    /**
     * 1h5 2小程序 3app 4微信原生jsapi 5沙盒 6钱包 7快捷
     */
    private Integer type;

    /**
     * 1支付宝 2微信 3苹果 4yeePay
     */
    private Integer payWay;

    /**
     * 苹果支付 sign、易宝支付token
     */
    private String sign;

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
     */
    private String remark;

    /**
     * 业务code 对应BusinessEnum枚举类
     */
    private Integer businessCode;

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
