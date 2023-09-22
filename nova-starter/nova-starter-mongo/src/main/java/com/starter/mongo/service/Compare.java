package com.starter.mongo.service;

import java.io.Serializable;
import java.util.Collection;

/**
 * 查询条件封装
 *
 * @author wzh
 * @date 2023/6/13
 */
public interface Compare<LambdaQueryWrapper, R> extends Serializable {

    /**
     * eq 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper eq(R column, Object val) {
        return eq(true, column, val);
    }

    /**
     * ne 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper ne(R column, Object val) {
        return ne(true, column, val);
    }

    /**
     * le 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper le(R column, Object val) {
        return le(true, column, val);
    }

    /**
     * lt 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper lt(R column, Object val) {
        return lt(true, column, val);
    }

    /**
     * ge 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper ge(R column, Object val) {
        return ge(true, column, val);
    }

    /**
     * gt 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper gt(R column, Object val) {
        return gt(true, column, val);
    }

    /**
     * between 条件构建 左右均包括
     *
     * @param column 参与比较的列
     * @param leftV  左边比较值
     * @param rightV 右边比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper between(R column, Object leftV, Object rightV) {
        return between(true, column, leftV, rightV);
    }

    /**
     * in 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper in(R column, Collection<Object> val) {
        return in(true, column, val);
    }

    /**
     * notIn 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper notIn(R column, Collection<Object> val) {
        return notIn(true, column, val);
    }

    /**
     * in 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper eq(boolean condition, R column, Object val);

    /**
     * ne 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper ne(boolean condition, R column, Object val);

    /**
     * le 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper le(boolean condition, R column, Object val);

    /**
     * lt 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper lt(boolean condition, R column, Object val);

    /**
     * ge 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper ge(boolean condition, R column, Object val);

    /**
     * gt 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper gt(boolean condition, R column, Object val);

    /**
     * between 条件构建 左右均包括
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param leftV     左边比较值
     * @param rightV    右边比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper between(boolean condition, R column, Object leftV, Object rightV);

    /**
     * in 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper in(boolean condition, R column, Collection<Object> val);

    /**
     * notIn 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper notIn(boolean condition, R column, Collection<Object> val);

}
