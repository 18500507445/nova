package com.nova.book.effectivejava.chapter7.section3;

import cn.hutool.core.collection.CollUtil;
import com.nova.common.utils.list.ListUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest3 {

    @Test
    public void demoA() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(3, 4, 5, 6);

        List<Integer> interSection = ListUtils.getInterSection(list1, list2);
        System.err.println(interSection);

        Collection<Integer> intersection = CollUtil.intersection(list1, list2);
        System.err.println(intersection);

    }


}
