package com.nova.tools.guava;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/10/13 18:24
 */
public class ArrayUtilsTest {

    /**
     * 给已有数组添加元素
     */
    @Test
    public void test2(){
        int[] ints = new int[1];
        ints[0] = 3;
        // toString打印数组内容
        System.out.println(ArrayUtils.toString(ints));
        // add会自动帮你创建新的数组
        int[] newArr = ArrayUtils.add(ints, 7);
        System.out.println(ArrayUtils.toString(ints));
        System.out.println(ArrayUtils.toString(newArr));

    }

    /**
     * 判断一个数组是不是空的(null/长度为0)
     */
    @Test
    public void test1(){
        Integer[] ints = new Integer[0];
        ints = null;
        System.out.println(ArrayUtils.isEmpty(ints));
    }
}
