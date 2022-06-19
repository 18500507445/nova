package com.nova.tools.demo.array;


import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/4/18 17:04
 */

public class Sort {
    public static void main(String[] args) {
        int[] arr1 = {33, 23, 78, 90, 1, 5};

        int[] arr2 = {4, 2, 6, 99, 100};

        System.out.println("合并数组");
        int[] ints = ArrayUtils.addAll(arr1, arr2);
        Arrays.sort(ints);
        for (int i : ints) {
            System.out.println(i);
        }

    }
}
