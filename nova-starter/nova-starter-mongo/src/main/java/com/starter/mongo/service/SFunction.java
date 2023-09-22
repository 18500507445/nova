package com.starter.mongo.service;

import java.io.Serializable;

/**
 * 支持序列化的 Function
 * 为了获取字段名字
 *
 * @author wzh
 * @date 2023/6/13
 */
@FunctionalInterface
public interface SFunction<T, R> extends Serializable {

    R apply(T t);

}
