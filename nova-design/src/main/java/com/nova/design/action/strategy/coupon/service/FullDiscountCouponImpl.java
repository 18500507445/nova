package com.nova.design.action.strategy.coupon.service;

import com.nova.design.action.strategy.coupon.FullDiscountCoupon;

import java.math.BigDecimal;

/**
 * @description: 满减券
 * @author: wzh
 * @date: 2022/12/31 10:54
 */
public class FullDiscountCouponImpl implements CouponCalService<FullDiscountCoupon> {

    @Override
    public BigDecimal calPrice(BigDecimal price, FullDiscountCoupon couponInfo) {
        return price.compareTo(couponInfo.getThresholdPrice()) > 0
                // 如果总金额 > 满减门槛 则 减去优惠面额
                ? price.subtract(couponInfo.getDiscountPrice())
                : price;
    }
}