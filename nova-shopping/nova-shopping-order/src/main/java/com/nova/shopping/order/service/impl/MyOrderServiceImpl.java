package com.nova.shopping.order.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import com.nova.shopping.order.dao.MyOrderDao;
import com.nova.shopping.order.entity.MyOrder;
import com.nova.shopping.order.service.MyOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author wzh
 * @description: 订单表(MyOrder)表服务实现类
 * @date 2023-04-15 15:53
 */
@Service("myOrderService")
public class MyOrderServiceImpl implements MyOrderService {


    @Override
    public String getOrderId(Map<String, String> map) {
        Snowflake snowflake = IdUtil.getSnowflake(31, 31);
        return snowflake.nextIdStr();
    }

    @Override
    public void successOrderHandler(String source, String sid, int businessCode, String orderId, String userName, String tradeStatus, String amount) {
        //延迟1s防止加款没走完导致扣费展示余额不足
        ThreadUtil.safeSleep(1000);

        //todo 开通业务
    }

    @Resource
    private MyOrderDao myOrderDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MyOrder queryById(Long id) {
        return myOrderDao.queryById(id);
    }

    /**
     * 分页查询list
     *
     * @param myOrder 筛选条件
     * @return 查询结果
     */
    @Override
    public List<MyOrder> queryList(MyOrder myOrder) {
        return myOrderDao.queryList(myOrder);
    }

    /**
     * 新增数据
     *
     * @param myOrder 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(MyOrder myOrder) {
        return myOrderDao.insert(myOrder);
    }

    /**
     * 修改数据
     *
     * @param myOrder 实例对象
     * @return 实例对象
     */
    @Override
    public int update(MyOrder myOrder) {
        return myOrderDao.update(myOrder);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return myOrderDao.deleteById(id) > 0;
    }

}
