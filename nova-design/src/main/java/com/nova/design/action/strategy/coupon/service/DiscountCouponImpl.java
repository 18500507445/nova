package com.nova.design.action.strategy.coupon.service;

import java.math.BigDecimal;

/**
 * @description: 折扣券
 * @author: wzh
 * @date: 2022/12/31 10:54
 */
public class DiscountCouponImpl implements CouponCalService<BigDecimal> {

    @Override
    public BigDecimal calPrice(BigDecimal price, BigDecimal discount) {
        // ex：100*0.9
        return price.multiply(discount);
    }
}