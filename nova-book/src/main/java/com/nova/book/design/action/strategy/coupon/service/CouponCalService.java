package com.nova.book.design.action.strategy.coupon.service;

import java.math.BigDecimal;

/**
 * @description: 策略接口
 * @author: wzh
 * @date: 2022/12/31 10:54
 */
public interface CouponCalService<T> {

    /**
     * 计算优惠
     *
     * @param price      商品金额
     * @param couponInfo 优惠券信息
     * @return 最终优惠后的金额
     */
    BigDecimal calPrice(BigDecimal price, T couponInfo);
}
