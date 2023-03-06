package com.nova.book.algorithm.base;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @description: 二分查找
 * @author: wzh
 * @date: 2023/3/5 14:36
 */
class BinarySearch {

    private static final int[] ARR = new int[]{1, 3, 5, 7, 8, 9, 11, 15, 16};

    private static final int[] MOST_ARR = new int[]{1, 3, 5, 5, 5, 7, 8, 9, 11, 15, 16};

    /**
     * 二分查找基础版
     * 有序数组A内，查找值target
     * <p>
     * f(n) = 5(log2^n+1)+4
     * 时间复杂度 O(log2^n)
     * 空间复杂度 int i、j、m 分别4个字节，一共12字节 所以是 O(1)
     */
    public static int binarySearchBase(int[] arr, int target) {
        int i = 0, j = arr.length - 1;
        while (i <= j) {
            int m = (i + j) / 2;
            //循环L次 ，元素在最左边L次，再最右边 if和else if都会进入判断，所以是2L次。引申出平衡版
            if (target < arr[m]) {
                j = m - 1;
            } else if (arr[m] < target) {
                i = m + 1;
            } else {
                return m;
            }
        }
        return -1;
    }

    /**
     * 升级版
     */
    public static int binarySearchPro(int[] arr, int target) {
        int i = 0, j = arr.length;
        while (i < j) {
            int m = (i + j) / 2;
            if (target < arr[m]) {
                j = m;
            } else if (arr[m] < target) {
                i = m + 1;
            } else {
                return m;
            }
        }
        return -1;
    }

    /**
     * 平衡版
     */
    public static int binarySearchBalance(int[] arr, int target) {
        int i = 0, j = arr.length;
        while (i < j - 1) {
            int m = (i + j) / 2;
            if (target < arr[m]) {
                j = m;
            } else {
                i = m;
            }
        }
        if (arr[i] == target) {
            return i;
        } else {
            return -1;
        }
    }

    /**
     * java中如何实现的二分查找
     */
    public static int binarySearchJava(int[] arr, int target) {
        return Arrays.binarySearch(arr, target);
    }

    /**
     * 二分查找 Leftmost
     * 查找相同的元素最左边的下标
     */
    public static int binarySearchLeftmost(int[] arr, int target) {
        int i = 0, j = arr.length - 1;
        int candidate = -1;
        while (i <= j) {
            int m = (i + j) / 2;
            if (target < arr[m]) {
                j = m - 1;
            } else if (arr[m] < target) {
                i = m + 1;
            } else {
                //相等的时候，记录候选位置，继续向左走
                candidate = m;
                j = m - 1;
            }
        }
        return candidate;
    }

    /**
     * 如果不存在数组中，返回一个比目标大的左边的第一个下标
     * <p>
     * 合并解释：返回>=target的最靠左索引
     */
    public static int binarySearchLeftmostPro(int[] arr, int target) {
        int i = 0, j = arr.length - 1;
        while (i <= j) {
            int m = (i + j) / 2;
            if (target <= arr[m]) {
                j = m - 1;
            } else {
                i = m + 1;
            }
        }
        return i;
    }

    /**
     * 线性查找
     * <p>
     * 执行步骤 n个元素
     * int i = 0； 1步
     * i< arr.length； n+1步
     * i++； n步
     * if(arr[i] == target)； n步
     * return i；1步
     * <p>
     * f(n) = 3n+3 --> 时间复杂度 O(n);
     */
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    @Test
    public void demoA() {
        int index = binarySearchBase(ARR, 5);
        Assert.equals(2, index);
    }

    @Test
    public void demoB() {
        int index = binarySearchPro(ARR, 7);
        Assert.equals(3, index);
    }

    @Test
    public void demoC() {
        int index = linearSearch(ARR, 5);
        Assert.equals(2, index);
    }

    @Test
    public void demoD() {
        int index = binarySearchJava(ARR, 18);
        System.out.println("index = " + index);
    }

    @Test
    public void demoE() {
        int index = binarySearchLeftmost(MOST_ARR, 5);
        System.out.println("index = " + index);
    }


}
