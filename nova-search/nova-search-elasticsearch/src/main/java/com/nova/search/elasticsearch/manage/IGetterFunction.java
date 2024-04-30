package com.nova.search.elasticsearch.manage;

import java.io.Serializable;

/**
 * @author wzh
 * @description: 函数式接口 便于方法引用获取实体字段名称
 */
@FunctionalInterface
@Deprecated
public interface IGetterFunction<T> extends Serializable {

    Object get(T source);

}