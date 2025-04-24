package com.nova.shopping.order.service;


import com.nova.shopping.order.repository.entity.User;

import java.util.List;

/**
 * @author wzh
 * @description: 用户表(MyUser)表服务接口
 * @date 2023-04-15 15:53
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Long id);

    /**
     * 分页查询list
     *
     * @param user 筛选条件
     * @return 查询结果
     */
    List<User> queryList(User user);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    int insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    int update(User user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
