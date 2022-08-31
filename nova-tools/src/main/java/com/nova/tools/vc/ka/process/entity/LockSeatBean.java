package com.nova.tools.vc.ka.process.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 锁定返回实体类
 * @Author: wangzehui
 * @Date: 2019/5/27 9:21
 */
@Data
public class LockSeatBean implements Serializable {
    private static final long serialVersionUID = -2088426626341690026L;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 锁座编码
     */
    private String lockCode;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 补贴金额
     */
    private BigDecimal subsidyPrice;
}
