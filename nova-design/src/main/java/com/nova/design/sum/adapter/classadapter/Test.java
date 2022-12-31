package com.nova.design.sum.adapter.classadapter;


import com.nova.design.sum.adapter.domain.PS2;

/**
 * @author: wzh
 * @date: 2019/5/19 11:39
 * @description:
 */
public class Test {

    public static void main(String[] args) {
        PS2 p = new PS2ToUsberAdapter();
        p.isPs2();
    }

}
