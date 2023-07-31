package com.starter.mongo.service;

import java.io.Serializable;

/**
 * 功能接口
 *
 * @author loser
 * @date 2023/6/13
 */
public interface Func<T, LambdaQueryWrapper, R extends SFunction<T, ?>> extends Serializable {

    /**
     * 升序 排序接口
     *
     * @param column 参与排序的列
     * @return 当前构建器
     */
    default LambdaQueryWrapper orderByAsc(R column) {
        return orderByAsc(true, column);
    }

    /**
     * 降序 排序接口
     *
     * @param column 参与排序的列
     * @return 当前构建器
     */
    default LambdaQueryWrapper orderByDesc(R column) {
        return orderByDesc(true, column);
    }

    /**
     * 升序 排序接口
     *
     * @param condition 是否参与排序
     * @param column    参与排序的列
     * @return 当前构建器
     */
    LambdaQueryWrapper orderByAsc(boolean condition, R column);

    /**
     * 降序 排序接口
     *
     * @param condition 是否参与排序
     * @param column    参与排序的列
     * @return 当前构建器
     */
    LambdaQueryWrapper orderByDesc(boolean condition, R column);

    /**
     * 分页skip接口
     *
     * @param skip 跳过多少条数据
     * @return 当前构建器
     */
    LambdaQueryWrapper skip(Long skip);


    /**
     * 分页limit接口
     *
     * @param limit 最多取多少条数据
     * @return 当前构建器
     */
    LambdaQueryWrapper limit(Integer limit);

    /**
     * 需要查询的列,不设置默认查询全部
     *
     * @param columns 查询的列
     * @return 当前构建器
     */
    LambdaQueryWrapper select(R... columns);

}
