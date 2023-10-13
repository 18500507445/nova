package com.nova.shopping.pay.service;


import com.nova.shopping.pay.entity.MyPayOrder;

import java.util.List;


/**
 * @author wzh
 * @description: 支付订单表明细表(MyPayOrder)表服务接口
 * @date 2023-04-14 19:27
 */
public interface MyPayOrderService {

    /**
     * 查询支付订单明细
     *
     * @param id 支付订单明细主键
     * @return 支付订单明细
     */
    MyPayOrder selectMyPayOrderById(Long id);

    /**
     * 查询支付订单明细列表
     *
     * @param myPayOrder 支付订单明细
     * @return 支付订单明细集合
     */
    List<MyPayOrder> selectMyPayOrderList(MyPayOrder myPayOrder);

    /**
     * 新增支付订单明细
     *
     * @param myPayOrder 支付订单明细
     * @return 结果
     */
    int insertMyPayOrder(MyPayOrder myPayOrder);

    /**
     * 修改支付订单明细
     *
     * @param myPayOrder 支付订单明细
     * @return 结果
     */
    int updateMyPayOrder(MyPayOrder myPayOrder);

    /**
     * 批量删除支付订单明细
     *
     * @param ids 需要删除的支付订单明细主键集合
     * @return 结果
     */
    int deleteMyPayOrderByIds(String ids);

    /**
     * 删除支付订单明细信息
     *
     * @param id 支付订单明细主键
     * @return 结果
     */
    int deleteMyPayOrderById(Long id);

    /**
     * 查询订单支付信息
     *
     * @param orderId 订单ID
     * @param payWay  支付类型
     * @return 支付订单信息
     */
    MyPayOrder selectMyPayOrderByOrderIdAndPayWay(String orderId, int payWay);

}
