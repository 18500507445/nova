package com.starter.mongo.service;


import com.starter.mongo.entity.Page;
import com.starter.mongo.wrapper.LambdaQueryWrapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * mongo 基础方法接口
 *
 * @author wzh
 * @date 2023/6/13
 */
public interface EasyMongoService<T> {

    /**
     * 查询单条数据
     *
     * @param queryWrapper 条件构造器
     * @return 查询到的集合数据
     */
    T getOne(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 保存新的数据
     *
     * @param entity 需要保存的实体
     * @return 是否保存成功
     */
    boolean save(T entity);

    /**
     * 批量保存新的数据 内部递归调用单个保存
     *
     * @param entityList 需要保存的数据列表
     * @return 是否保存成功
     */
    boolean saveBatch(Collection<T> entityList);

    /**
     * 通过ID删除数据
     *
     * @param id 数据ID
     * @return 是否删除成功
     */
    boolean removeById(Serializable id);

    /**
     * 通过条件构建器删除数据
     *
     * @param queryWrapper 条件构建器
     * @return 是否删除成功
     */
    boolean remove(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过ID更新数据 只有存在数据的字段才会更新
     *
     * @param entity 需要更新的数据
     * @return 是否更新成功
     */
    boolean updateById(T entity);

    /**
     * 通过条件构造器更新数据 只有存在数据的字段才会更新
     *
     * @param entity       需要更新的数据
     * @param queryWrapper 条件构建起
     * @return 是否更新成功
     */
    boolean update(T entity, LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过ID获取数据
     *
     * @param id 数据ID
     * @return 集合中的数据
     */
    T getById(Serializable id);

    /**
     * 通过数据ID集合获取数据集合
     *
     * @param idList 数据ID集合
     * @return 查询到的数据集合
     */
    Collection<T> listByIds(Collection<? extends Serializable> idList);

    /**
     * 通过条件构建起统计数据量
     *
     * @param queryWrapper 条件构建起
     * @return 数据两
     */
    long count(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构建起查询列表
     *
     * @param queryWrapper 条件构建器
     * @return 数据集合
     */
    List<T> list(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构建起进行分页查询
     *
     * @param queryWrapper 条件构建器
     * @param pageNo       页面
     * @param pageSize     页面大小
     * @return 分页对象
     */
    Page<T> page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize);

    /**
     * 通过条件构建器判断是否存在数据
     *
     * @param queryWrapper 条件构建器
     * @return 是否存在数据
     */
    boolean exist(LambdaQueryWrapper<T> queryWrapper);

}
