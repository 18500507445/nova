package com.nova.shopping.order.service;


import com.nova.shopping.order.entity.MyOrder;

import java.util.List;
import java.util.Map;

/**
 * @author wzh
 * @description: 订单表(MyOrder)表服务接口
 * @date 2023-04-15 15:53
 */
public interface MyOrderService {

    String getOrderId(Map<String, String> paramsMap);

    /**
     * 成功订单处理
     *
     * @param source
     * @param sid
     * @param businessCode
     * @param orderId
     * @param userName
     * @param tradeStatus
     * @param amount
     */
    void successOrderHandler(String source, String sid, int businessCode, String orderId, String userName, String tradeStatus, String amount);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MyOrder queryById(Long id);

    /**
     * 分页查询list
     *
     * @param myOrder 筛选条件
     * @return 查询结果
     */
    List<MyOrder> queryList(MyOrder myOrder);

    /**
     * 新增数据
     *
     * @param myOrder 实例对象
     * @return 实例对象
     */
    int insert(MyOrder myOrder);

    /**
     * 修改数据
     *
     * @param myOrder 实例对象
     * @return 实例对象
     */
    int update(MyOrder myOrder);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
