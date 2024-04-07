package com.nova.shopping.pay.repository.mapper;

import com.nova.shopping.pay.repository.entity.PayOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wzh
 * @description: 支付订单表明细表(MyPayOrder)表数据库访问层
 * @date 2023-04-14 19:27
 */
@Mapper
public interface PayOrderDao {

    /**
     * 查询支付订单明细
     *
     * @param id 支付订单明细主键
     * @return 支付订单明细
     */
    PayOrder selectMyPayOrderById(Long id);

    /**
     * 查询支付订单明细列表
     *
     * @param payOrder 支付订单明细
     * @return 支付订单明细集合
     */
    List<PayOrder> selectMyPayOrderList(PayOrder payOrder);

    /**
     * 新增支付订单明细
     *
     * @param payOrder 支付订单明细
     * @return 结果
     */
    int insertMyPayOrder(PayOrder payOrder);

    /**
     * 修改支付订单明细
     *
     * @param payOrder 支付订单明细
     * @return 结果
     */
    int updateMyPayOrder(PayOrder payOrder);

    /**
     * 删除支付订单明细
     *
     * @param id 支付订单明细主键
     * @return 结果
     */
    int deleteMyPayOrderById(Long id);

    /**
     * 批量删除支付订单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMyPayOrderByIds(String[] ids);

    PayOrder selectMyPayOrderByOrderIdAndPayWay(@Param("orderId") String orderId, @Param("payWay") int payWay);

}

