package com.nova.book.design.structure.decorator.coupon.service.impl;

import cn.hutool.json.JSONUtil;
import com.nova.book.design.structure.decorator.coupon.model.Discount;
import com.nova.book.design.structure.decorator.coupon.model.OrderDTO;
import com.nova.book.design.structure.decorator.coupon.model.Product;
import com.nova.book.design.structure.decorator.coupon.service.OrderService;

import java.util.List;

/**
 * @description: 订单 业务实现类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class OrderServiceImpl implements OrderService {

    @Override
    public void createOrder(OrderDTO order) {
        System.out.println("订单参数：" + JSONUtil.toJsonStr(order));
        List<Product> productList = order.getProductList();
        List<Discount> discountList = order.getDiscountList();

    }
}
