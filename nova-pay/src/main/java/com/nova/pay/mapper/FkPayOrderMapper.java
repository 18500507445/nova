package com.nova.pay.mapper;

import com.nova.pay.entity.result.FkPayOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 支付订单明细Mapper接口
 * @Author: wangzehui
 * @Date: 2022/8/22 15:48
 */
@Mapper
public interface FkPayOrderMapper {

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
     * 删除支付订单明细
     *
     * @param id 支付订单明细主键
     * @return 结果
     */
    int deleteFkPayOrderById(Long id);

    /**
     * 批量删除支付订单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteFkPayOrderByIds(String[] ids);

    FkPayOrder selectNtPayOrderByOrderIdAndPayWay(@Param("orderId") String orderId, @Param("payWay") int payWay);
}
