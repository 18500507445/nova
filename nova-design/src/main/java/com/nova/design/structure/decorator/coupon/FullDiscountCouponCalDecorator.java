package com.nova.design.structure.decorator.coupon;

import java.math.BigDecimal;

/**
 * @description: 具体装饰 - 满减券
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class FullDiscountCouponCalDecorator extends AbstractCouponCalDecorator {

    /**
     * 满减门槛
     */
    private final BigDecimal thresholdPrice;

    /**
     * 满减优惠面额
     */
    private final BigDecimal discountPrice;

    public FullDiscountCouponCalDecorator(CouponCal couponCal, BigDecimal thresholdPrice, BigDecimal discountPrice) {
        super(couponCal);
        this.thresholdPrice = thresholdPrice;
        this.discountPrice = discountPrice;
    }

    @Override
    public BigDecimal price() {
        return this.couponCal.price().compareTo(this.thresholdPrice) > 0
                // 如果总金额 > 满减门槛 则 减去优惠面额
                ? this.couponCal.price().subtract(this.discountPrice)
                : this.couponCal.price();
    }
}
