package com.nova.design.action.interceptor.service;

import com.nova.design.action.interceptor.Context;

/**
 * @description: 解释器接口
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
public interface Expression {

    int interpreter(Context context);
}