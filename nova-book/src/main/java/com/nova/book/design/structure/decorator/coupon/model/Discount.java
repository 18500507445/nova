package com.nova.book.design.structure.decorator.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @description: 优惠券信息
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {
    /**
     * 优惠券类型
     */
    private Integer type;
    /**
     * 优惠券ID
     */
    private Long couponId;
    /**
     * 优惠券数量
     */
    private Integer num;
}