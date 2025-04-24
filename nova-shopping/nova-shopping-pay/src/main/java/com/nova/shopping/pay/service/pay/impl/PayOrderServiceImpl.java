package com.nova.shopping.pay.service.pay.impl;

import cn.hutool.core.convert.Convert;
import com.nova.shopping.pay.repository.mapper.PayOrderDao;
import com.nova.shopping.pay.repository.entity.PayOrder;
import com.nova.shopping.pay.service.pay.PayOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: wzh
 * @description: 支付订单表明细表(MyPayOrder)表服务实现类
 * @date: 2023-04-14 19:27
 */
@Service("myPayOrderService")
@RequiredArgsConstructor
public class PayOrderServiceImpl implements PayOrderService {

    private final PayOrderDao payOrderDao;

    /**
     * 查询支付订单明细
     *
     * @param id 支付订单明细主键
     * @return 支付订单明细
     */
    @Override
    public PayOrder selectMyPayOrderById(Long id) {
        return payOrderDao.selectMyPayOrderById(id);
    }

    /**
     * 查询支付订单明细列表
     *
     * @param payOrder 支付订单明细
     * @return 支付订单明细
     */
    @Override
    public List<PayOrder> selectMyPayOrderList(PayOrder payOrder) {
        return payOrderDao.selectMyPayOrderList(payOrder);
    }

    /**
     * 新增支付订单明细
     *
     * @param payOrder 支付订单明细
     * @return 结果
     */
    @Override
    public int insertMyPayOrder(PayOrder payOrder) {
        return payOrderDao.insertMyPayOrder(payOrder);
    }

    /**
     * 修改支付订单明细
     *
     * @param payOrder 支付订单明细
     * @return 结果
     */
    @Override
    public int updateMyPayOrder(PayOrder payOrder) {
        return payOrderDao.updateMyPayOrder(payOrder);
    }

    /**
     * 批量删除支付订单明细
     *
     * @param ids 需要删除的支付订单明细主键
     * @return 结果
     */
    @Override
    public int deleteMyPayOrderByIds(String ids) {
        return payOrderDao.deleteMyPayOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付订单明细信息
     *
     * @param id 支付订单明细主键
     * @return 结果
     */
    @Override
    public int deleteMyPayOrderById(Long id) {
        return payOrderDao.deleteMyPayOrderById(id);
    }

    @Override
    public PayOrder selectMyPayOrderByOrderIdAndPayWay(String orderId, int payWay) {
        return payOrderDao.selectMyPayOrderByOrderIdAndPayWay(orderId, payWay);
    }
}
