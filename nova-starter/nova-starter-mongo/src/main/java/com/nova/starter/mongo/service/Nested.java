package com.nova.starter.mongo.service;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 查询条件封装
 * <p>嵌套</p>
 *
 * @author wzh
 * @date 2023/6/13
 */
public interface Nested<Param, LambdaQueryWrapper> extends Serializable {

    /**
     * 嵌套条件构造器
     *
     * @param func 嵌套条件
     * @return 当前条件构造器
     */
    default LambdaQueryWrapper and(Function<Param, Param> func) {
        return and(true, func);
    }

    /**
     * 嵌套条件构造器 使用or连接
     *
     * @return 当前条件构造器
     */
    LambdaQueryWrapper or();

    /**
     * 嵌套条件构造器
     *
     * @param condition 是否开启嵌套
     * @param func      嵌套条件
     * @return 当前条件构造器
     */
    LambdaQueryWrapper and(boolean condition, Function<Param, Param> func);

}
