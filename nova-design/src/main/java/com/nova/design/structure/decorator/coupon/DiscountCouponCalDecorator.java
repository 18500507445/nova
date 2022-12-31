package com.nova.design.structure.decorator.coupon;

import java.math.BigDecimal;

/**
 * @description: 具体装饰 - 折扣券
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class DiscountCouponCalDecorator extends AbstractCouponCalDecorator {

    /**
     * 折扣 ex: 0.9
     */
    private final BigDecimal discount;

    public DiscountCouponCalDecorator(CouponCal couponCal, BigDecimal discount) {
        super(couponCal);
        this.discount = discount;
    }

    @Override
    public BigDecimal price() {
        return this.couponCal.price().multiply(this.discount);
    }
}
