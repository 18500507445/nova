package com.nova.pay.service.fk;

import com.nova.pay.entity.result.FkPayOrder;

import java.util.List;

/**
 * @description: 支付订单明细Service接口
 * @author: wzh
 * @date: 2022/8/22 15:49
 */
public interface FkPayOrderService {

    /**
     * 查询支付订单明细
     *
     * @param id 支付订单明细主键
     * @return 支付订单明细
     */
    FkPayOrder selectFkPayOrderById(Long id);


    /**
     * 查询支付订单明细列表
     *
     * @param fkPayOrder 支付订单明细
     * @return 支付订单明细集合
     */
    List<FkPayOrder> selectFkPayOrderList(FkPayOrder fkPayOrder);

    /**
     * 新增支付订单明细
     *
     * @param fkPayOrder 支付订单明细
     * @return 结果
     */
    int insertFkPayOrder(FkPayOrder fkPayOrder);

    /**
     * 修改支付订单明细
     *
     * @param fkPayOrder 支付订单明细
     * @return 结果
     */
    int updateFkPayOrder(FkPayOrder fkPayOrder);

    /**
     * 批量删除支付订单明细
     *
     * @param ids 需要删除的支付订单明细主键集合
     * @return 结果
     */
    int deleteFkPayOrderByIds(String ids);

    /**
     * 删除支付订单明细信息
     *
     * @param id 支付订单明细主键
     * @return 结果
     */
    int deleteFkPayOrderById(Long id);

    /**
     * 查询订单支付信息
     *
     * @param orderId 订单ID
     * @param payWay  支付类型
     * @return 支付订单信息
     */
    FkPayOrder selectNtPayOrderByOrderIdAndPayWay(String orderId, int payWay);

}
