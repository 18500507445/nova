package com.nova.shopping.order.service;


import com.nova.shopping.order.entity.MyGoods;

import java.util.List;

/**
 * @author wzh
 * @description: 商品表(MyGoods)表服务接口
 * @date 2023-04-15 15:53
 */
public interface MyGoodsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MyGoods queryById(Long id);

    /**
     * 分页查询list
     *
     * @param myGoods 筛选条件
     * @return 查询结果
     */
    List<MyGoods> queryList(MyGoods myGoods);

    /**
     * 新增数据
     *
     * @param myGoods 实例对象
     * @return 实例对象
     */
    int insert(MyGoods myGoods);

    /**
     * 修改数据
     *
     * @param myGoods 实例对象
     * @return 实例对象
     */
    int update(MyGoods myGoods);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
