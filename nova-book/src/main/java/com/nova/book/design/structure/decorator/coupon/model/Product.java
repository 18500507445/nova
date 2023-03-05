package com.nova.book.design.structure.decorator.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @description: 商品信息
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品价格
     */
    private BigDecimal price;
    /**
     * 商品数量
     */
    private int num;
}