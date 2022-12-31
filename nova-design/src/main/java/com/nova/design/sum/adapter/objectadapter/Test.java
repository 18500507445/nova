package com.nova.design.sum.adapter.objectadapter;


import com.nova.design.sum.adapter.domain.PS2;
import com.nova.design.sum.adapter.domain.Usber;

/**
 * @author: wzh
 * @date: 2019/5/19 11:55
 * @description:
 */
public class Test {

    public static void main(String[] args) {
        PS2 p = new PS2ToUsberAdapter(new Usber());
        p.isPs2();
    }

}
