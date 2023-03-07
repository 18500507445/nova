package com.nova.book.algorithm.base;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @description: 递归
 * @author: wzh
 * @date: 2023/3/7 18:11
 */
class Recursion {

    /**
     * 求阶乘
     * 阶乘的定义 n!= 1⋅2⋅3⋯(n-2)⋅(n-1)⋅n，其中 n 为自然数，当然 0! = 1
     */
    public int factorial(int n) {
        if (n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    @Test
    public void factorial() {
        int sum = factorial(5);
        System.out.println("sum = " + sum);
    }

    /**
     * 反向打印字符串
     */
    public void reversePrintString(String str, int n) {
        if (n == str.length()) {
            return;
        }
        reversePrintString(str, n + 1);
        System.out.println(str.charAt(n));
    }

    @Test
    public void print() {
        reversePrintString("abc", 0);
    }

    /**
     * 递归二分查找
     *
     * @param arr
     * @param target
     * @param i      两个指针边界
     * @param j
     * @return
     */
    public int binarySearch(int[] arr, int target, int i, int j) {
        if (i > j) {
            return -1;
        }
        int m = (i + j) / 2;
        if (target < arr[m]) {
            return binarySearch(arr, target, i, m - 1);
        } else if (arr[m] < target) {
            return binarySearch(arr, target, i + 1, j);
        } else {
            return m;
        }
    }

    @Test
    public void binarySearch() {
        int[] arr = {1, 3, 5, 7, 9};
        int index = binarySearch(arr, 3, 0, arr.length - 1);
        System.out.println("index = " + index);
    }

    /**
     * 递归冒泡排序
     * 每次交换将i的值赋值给x，也就是说x左边是无序的，右边是有序的
     * @param arr 数组
     * @param j   未排序区域右边界
     */
    public void bubble(int[] arr, int j) {
        if (j == 0) {
            return;
        }
        int x = 0;
        for (int i = 0; i < j; i++) {
            if (arr[i] > arr[i + 1]) {
                int temp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = temp;
                x = i;
            }
        }
        bubble(arr, x);
    }

    @Test
    public void bubble() {
        int[] arr = {7, 5, 3, 1, 9};
        bubble(arr, arr.length - 1);
        System.out.println("arr:" + Arrays.toString(arr));
    }


}
