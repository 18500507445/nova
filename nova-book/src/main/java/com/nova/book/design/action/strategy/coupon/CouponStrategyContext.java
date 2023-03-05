package com.nova.book.design.action.strategy.coupon;

import com.nova.book.design.action.strategy.coupon.service.CouponCalService;

import java.math.BigDecimal;

/**
 * @description: 策略上下文
 * @author: wzh
 * @date: 2022/12/31 10:54
 */
public class CouponStrategyContext<T> {

    /**
     * 优惠策略
     */
    private final CouponCalService<T> couponCalService;

    public CouponStrategyContext(CouponCalService<T> couponCalService) {
        this.couponCalService = couponCalService;
    }

    public BigDecimal calPrice(BigDecimal price, T couponInfo) {
        return this.couponCalService.calPrice(price, couponInfo);
    }

}
