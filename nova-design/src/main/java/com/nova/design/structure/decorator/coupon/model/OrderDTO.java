package com.nova.design.structure.decorator.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 订单
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    /**
     * 商品明细
     */
    private List<Product> productList;

    /**
     * 优惠明细
     */
    private List<Discount> discountList;

}
