package com.nova.design.action.interceptor;

import com.nova.design.action.interceptor.service.Expression;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 环境类，存储解释器之外的一些全局信息，通常它临时存储了需要解释的语句
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
public class Context {

    private final Map<Expression, Integer> map = new HashMap<>();

    public void add(Expression s, Integer value) {
        this.map.put(s, value);
    }

    public int lookup(Expression s) {
        // 将变量转换成数字
        return this.map.get(s);
    }
}