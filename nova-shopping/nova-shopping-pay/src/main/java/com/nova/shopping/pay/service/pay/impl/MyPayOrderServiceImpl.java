package com.nova.shopping.pay.service.pay.impl;

import cn.hutool.core.convert.Convert;
import com.nova.shopping.pay.dao.MyPayOrderDao;
import com.nova.shopping.pay.entity.MyPayOrder;
import com.nova.shopping.pay.service.pay.MyPayOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wzh
 * @description: 支付订单表明细表(MyPayOrder)表服务实现类
 * @date 2023-04-14 19:27
 */
@Service("myPayOrderService")
@RequiredArgsConstructor
public class MyPayOrderServiceImpl implements MyPayOrderService {

    private final MyPayOrderDao myPayOrderDao;

    /**
     * 查询支付订单明细
     *
     * @param id 支付订单明细主键
     * @return 支付订单明细
     */
    @Override
    public MyPayOrder selectMyPayOrderById(Long id) {
        return myPayOrderDao.selectMyPayOrderById(id);
    }

    /**
     * 查询支付订单明细列表
     *
     * @param myPayOrder 支付订单明细
     * @return 支付订单明细
     */
    @Override
    public List<MyPayOrder> selectMyPayOrderList(MyPayOrder myPayOrder) {
        return myPayOrderDao.selectMyPayOrderList(myPayOrder);
    }

    /**
     * 新增支付订单明细
     *
     * @param myPayOrder 支付订单明细
     * @return 结果
     */
    @Override
    public int insertMyPayOrder(MyPayOrder myPayOrder) {
        return myPayOrderDao.insertMyPayOrder(myPayOrder);
    }

    /**
     * 修改支付订单明细
     *
     * @param myPayOrder 支付订单明细
     * @return 结果
     */
    @Override
    public int updateMyPayOrder(MyPayOrder myPayOrder) {
        return myPayOrderDao.updateMyPayOrder(myPayOrder);
    }

    /**
     * 批量删除支付订单明细
     *
     * @param ids 需要删除的支付订单明细主键
     * @return 结果
     */
    @Override
    public int deleteMyPayOrderByIds(String ids) {
        return myPayOrderDao.deleteMyPayOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付订单明细信息
     *
     * @param id 支付订单明细主键
     * @return 结果
     */
    @Override
    public int deleteMyPayOrderById(Long id) {
        return myPayOrderDao.deleteMyPayOrderById(id);
    }

    @Override
    public MyPayOrder selectMyPayOrderByOrderIdAndPayWay(String orderId, int payWay) {
        return myPayOrderDao.selectMyPayOrderByOrderIdAndPayWay(orderId, payWay);
    }
}
