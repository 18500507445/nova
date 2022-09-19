package com.nova.design.adapter.classadapter;


import com.nova.design.adapter.domain.PS2;
import com.nova.design.adapter.domain.Usber;

/**
 * @author: landy
 * @date: 2019/5/19 11:37
 * @description:
 */
public class PS2ToUsberAdapter extends Usber implements PS2 {

    @Override
    public boolean isPs2() {
        System.out.println("PS2接口转化为了USB接口");
        return isUsb();
    }
}
