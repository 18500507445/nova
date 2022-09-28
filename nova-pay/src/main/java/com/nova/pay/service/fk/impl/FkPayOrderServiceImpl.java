package com.nova.pay.service.fk.impl;

import cn.hutool.core.convert.Convert;
import com.nova.pay.entity.result.FkPayOrder;
import com.nova.pay.mapper.FkPayOrderMapper;
import com.nova.pay.service.fk.FkPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 支付订单明细Service业务层处理
 * @Author: wangzehui
 * @Date: 2022/8/22 15:50
 */
@Service
public class FkPayOrderServiceImpl implements FkPayOrderService {

    @Autowired
    private FkPayOrderMapper fkPayOrderMapper;

    /**
     * 查询支付订单明细
     *
     * @param id 支付订单明细主键
     * @return 支付订单明细
     */
    @Override
    public FkPayOrder selectFkPayOrderById(Long id) {
        return fkPayOrderMapper.selectFkPayOrderById(id);
    }

    /**
     * 查询支付订单明细列表
     *
     * @param fkPayOrder 支付订单明细
     * @return 支付订单明细
     */
    @Override
    public List<FkPayOrder> selectFkPayOrderList(FkPayOrder fkPayOrder) {
        return fkPayOrderMapper.selectFkPayOrderList(fkPayOrder);
    }

    /**
     * 新增支付订单明细
     *
     * @param fkPayOrder 支付订单明细
     * @return 结果
     */
    @Override
    public int insertFkPayOrder(FkPayOrder fkPayOrder) {
        return fkPayOrderMapper.insertFkPayOrder(fkPayOrder);
    }

    /**
     * 修改支付订单明细
     *
     * @param fkPayOrder 支付订单明细
     * @return 结果
     */
    @Override
    public int updateFkPayOrder(FkPayOrder fkPayOrder) {
        return fkPayOrderMapper.updateFkPayOrder(fkPayOrder);
    }

    /**
     * 批量删除支付订单明细
     *
     * @param ids 需要删除的支付订单明细主键
     * @return 结果
     */
    @Override
    public int deleteFkPayOrderByIds(String ids) {
        return fkPayOrderMapper.deleteFkPayOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付订单明细信息
     *
     * @param id 支付订单明细主键
     * @return 结果
     */
    @Override
    public int deleteFkPayOrderById(Long id) {
        return fkPayOrderMapper.deleteFkPayOrderById(id);
    }

    @Override
    public FkPayOrder selectNtPayOrderByOrderIdAndPayWay(String orderId, int payWay) {
        return fkPayOrderMapper.selectNtPayOrderByOrderIdAndPayWay(orderId, payWay);
    }
}
