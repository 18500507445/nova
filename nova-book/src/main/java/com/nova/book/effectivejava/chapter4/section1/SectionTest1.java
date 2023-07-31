package com.nova.book.effectivejava.chapter4.section1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest1 {

    @Test
    public void demoA() {

        List error = new ArrayList();

        List<Integer> correct = new ArrayList<>();
    }

    @Test
    public void demoB() {
        List list = new ArrayList();
        list.add(1);
        list.add(2L);

        Integer i = (Integer) list.get(1);
        System.err.println(i);
    }


}
