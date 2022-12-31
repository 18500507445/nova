package com.nova.design.structure.decorator.coupon;


/**
 * @description: 抽象的装饰器 - 优惠券
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public abstract class AbstractCouponCalDecorator implements CouponCal {

    protected final CouponCal couponCal;

    public AbstractCouponCalDecorator(CouponCal couponCal) {
        this.couponCal = couponCal;
    }

}
