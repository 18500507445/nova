package com.nova.book.design.structure.decorator.coupon.service;


import com.nova.book.design.structure.decorator.coupon.model.OrderDTO;

/**
 * @description: 订单 业务类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param order 提交的订单数据
     * @return void
     * @author zhengqingya
     * @date 2022/12/15 14:58
     */
    void createOrder(OrderDTO order);

}
