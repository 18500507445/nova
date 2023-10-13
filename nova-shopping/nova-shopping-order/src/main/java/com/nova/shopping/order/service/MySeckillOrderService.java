package com.nova.shopping.order.service;


import com.nova.shopping.order.entity.MySeckillOrder;

import java.util.List;

/**
 * @author wzh
 * @description: 秒杀订单表(MySeckillOrder)表服务接口
 * @date 2023-04-15 15:53
 */
public interface MySeckillOrderService {

    /**
     * 通过ID查询单条数据
     *
     * @param userId  用户id
     * @param goodsId 商品id
     * @return 实例对象
     */
    MySeckillOrder queryById(Long userId, Long goodsId);

    /**
     * 分页查询list
     *
     * @param mySeckillOrder 筛选条件
     * @return 查询结果
     */
    List<MySeckillOrder> queryList(MySeckillOrder mySeckillOrder);

    /**
     * 新增数据
     *
     * @param mySeckillOrder 实例对象
     * @return 实例对象
     */
    int insert(MySeckillOrder mySeckillOrder);

    /**
     * 修改数据
     *
     * @param mySeckillOrder 实例对象
     * @return 实例对象
     */
    int update(MySeckillOrder mySeckillOrder);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
