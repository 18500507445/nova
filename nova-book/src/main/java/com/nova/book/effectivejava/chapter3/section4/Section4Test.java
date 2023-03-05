package com.nova.book.effectivejava.chapter3.section4;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/15 20:58
 */
class Section4Test {

    @Test
    public void demoA() {
        BList<Integer> b = new BList<>();
        b.add(1);
        b.add(2);
        b.addAll(Arrays.asList(4, 5, 6));

        CList<Integer> c = new CList<>();
        c.add(1);
        c.add(2);
        c.addAll(Arrays.asList(4, 5, 6));

        System.out.println("b.addCount() = " + b.addCount());
        System.out.println("c.addCount() = " + c.addCount());


    }

}
