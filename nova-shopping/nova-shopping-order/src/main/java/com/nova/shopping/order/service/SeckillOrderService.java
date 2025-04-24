package com.nova.shopping.order.service;


import com.nova.shopping.order.repository.entity.SeckillOrder;

import java.util.List;

/**
 * @author: wzh
 * @description: 秒杀订单表(MySeckillOrder)表服务接口
 * @date: 2023-04-15 15:53
 */
public interface SeckillOrderService {

    /**
     * 通过ID查询单条数据
     *
     * @param userId  用户id
     * @param goodsId 商品id
     * @return 实例对象
     */
    SeckillOrder queryById(Long userId, Long goodsId);

    /**
     * 分页查询list
     *
     * @param seckillOrder 筛选条件
     * @return 查询结果
     */
    List<SeckillOrder> queryList(SeckillOrder seckillOrder);

    /**
     * 新增数据
     *
     * @param seckillOrder 实例对象
     * @return 实例对象
     */
    int insert(SeckillOrder seckillOrder);

    /**
     * 修改数据
     *
     * @param seckillOrder 实例对象
     * @return 实例对象
     */
    int update(SeckillOrder seckillOrder);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
