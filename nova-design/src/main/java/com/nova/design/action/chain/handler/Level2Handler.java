package com.nova.design.action.chain.handler;

/**
 * @description: 二级审核
 * @author: wangzehui
 * @date: 2022/12/31 8:20
 */
public class Level2Handler extends AbstractHandler {

    @Override
    public boolean approve(Object param) {
        System.out.println("二级审核：" + param);
        // 默认都是通过，走下一级审核
        return super.next(param);
    }
}
