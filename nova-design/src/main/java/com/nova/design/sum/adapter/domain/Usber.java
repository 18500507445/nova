package com.nova.design.sum.adapter.domain;


/**
 * @author: landy
 * @date: 2019/5/19 11:36
 * @description:
 */
public class Usber implements USB {

    @Override
    public boolean isUsb() {
        System.out.println("这是一个USB接口");
        return true;
    }
}
