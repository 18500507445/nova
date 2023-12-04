package com.nova.book.design.action.strategy.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @description: 满减优惠券
 * @author: wzh
 * @date: 2022/12/31 10:53
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FullDiscountCoupon {

    /**
     * 满减门槛
     */
    private BigDecimal thresholdPrice;

    /**
     * 满减优惠面额
     */
    private BigDecimal discountPrice;
}
