package com.nova.tools.utils.guava;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/10/13 18:23
 */
public class CollectionsUtilsTest {

    /**
     * 交集、并集、差集
     */
    @Test
    public void test2() {
        List<String> list1 = new ArrayList<>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        List<String> list2 = new ArrayList<>();
        list2.add("c");
        list2.add("1");
        list2.add("2");
        // 取交集 ,c
        System.out.println(CollectionUtils.intersection(list1, list2));
        // 并集, a,b,c,1,2
        System.out.println(CollectionUtils.union(list1, list2));
        // 差集,a,b
        System.out.println(CollectionUtils.subtract(list1, list2));
        // 1,2
        System.out.println(CollectionUtils.subtract(list2, list1));
    }

    /**
     * 判断list、set不为空(null/size>0)
     */
    @Test
    public void test1() {
        List<String> list = new ArrayList<>();
        // true
        System.out.println(CollectionUtils.isEmpty(list));
        // 反过来
        System.out.println(CollectionUtils.isNotEmpty(list));
    }
}
