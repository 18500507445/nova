package com.nova.shopping.pay.dao;

import com.nova.shopping.pay.entity.MyPayOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wzh
 * @description: 支付订单表明细表(MyPayOrder)表数据库访问层
 * @date 2023-04-14 19:27
 */
@Mapper
public interface MyPayOrderDao {

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

}

