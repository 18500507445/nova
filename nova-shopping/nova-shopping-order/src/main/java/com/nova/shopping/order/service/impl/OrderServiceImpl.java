package com.nova.shopping.order.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import com.nova.shopping.order.repository.entity.Order;
import com.nova.shopping.order.repository.mapper.OrderDao;
import com.nova.shopping.order.service.OrderService;
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
public class OrderServiceImpl implements OrderService {


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
    private OrderDao orderDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Order queryById(Long id) {
        return orderDao.queryById(id);
    }

    /**
     * 分页查询list
     *
     * @param order 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Order> queryList(Order order) {
        return orderDao.queryList(order);
    }

    /**
     * 新增数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(Order order) {
        return orderDao.insert(order);
    }

    /**
     * 修改数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    @Override
    public int update(Order order) {
        return orderDao.update(order);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return orderDao.deleteById(id) > 0;
    }

}
