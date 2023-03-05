package com.nova.book.design.structure.decorator.coupon;


import com.nova.book.design.structure.decorator.coupon.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 订单
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class OrderCal implements CouponCal {

    /**
     * 商品明细
     */
    private final List<Product> productList;


    public OrderCal(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public BigDecimal price() {
        return this.productList.stream().map(e -> e.getPrice().multiply(BigDecimal.valueOf(e.getNum()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
