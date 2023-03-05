package com.nova.book.design.action.interpreter.service;

import com.nova.book.design.action.interpreter.Context;

/**
 * @description: 解释器接口
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
public interface Expression {

    int interpreter(Context context);
}