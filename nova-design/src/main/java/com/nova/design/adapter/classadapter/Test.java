package com.nova.design.adapter.classadapter;


import com.nova.design.adapter.domain.PS2;

/**
 * @author: landy
 * @date: 2019/5/19 11:39
 * @description:
 */
public class Test {

    public static void main(String[] args) {
        PS2 p = new PS2ToUsberAdapter();
        p.isPs2();
    }

}