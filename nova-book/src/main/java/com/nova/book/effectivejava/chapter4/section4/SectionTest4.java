package com.nova.book.effectivejava.chapter4.section4;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest4 {

    public <T> List<T> remove(List<T> t, int index) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < t.size(); i++) {
            if (index == i) {
                continue;
            }
            list.add(list.get(i));
        }
        return list;

    }

    @Test
    public void demoA() {

    }


}
