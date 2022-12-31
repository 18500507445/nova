package com.nova.design.action.chain.handler;

/**
 * @description: 一级审核
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
public class Level1Handler extends AbstractHandler {

    @Override
    public boolean approve(Object param) {
        System.out.println("一级审核：" + param);
        // 默认都是通过，走下一级审核
        return super.next(param);
    }
}