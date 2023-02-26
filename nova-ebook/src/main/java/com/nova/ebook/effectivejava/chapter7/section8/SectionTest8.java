package com.nova.ebook.effectivejava.chapter7.section8;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest8 {

    @Test
    public void demoA() {
        Vector<Object> objects = new Vector<>();

        List<Integer> list = new ArrayList<>();
    }

    /**
     * valueOf是integer特有的方法
     */
    @Test
    public void demoB() {
        Integer i = Integer.valueOf(10);
        Number num = i;
        int i1 = num.intValue();
    }


}
