package com.nova.tools.demo.datatype;


import cn.hutool.json.JSONUtil;
import com.nova.tools.demo.entity.Myself;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @Description: 排序练习类
 * @Author: wangzehui
 * @Date: 2019/4/18 17:04
 */
public class Sort {

    /**
     * 合并数组
     */
    @Test
    public void addAll() {
        int[] arr1 = {33, 23, 78, 90, 1, 5};
        int[] arr2 = {4, 2, 6, 99, 100};
        int[] ints = ArrayUtils.addAll(arr1, arr2);
        Arrays.sort(ints);
        for (int i : ints) {
            System.out.println(i);
        }
    }

    /**
     * 指定排序
     */
    @Test
    public void specifySort() {
        String filmType = "3D,2K,中国巨幕2K";
        String[] s = Myself.FILM_ARR;
        String[] split = filmType.split(",");
        StringBuilder a = new StringBuilder();
        if (split.length > 0) {
            for (String value : s) {
                for (String s1 : split) {
                    if (s1.equals(value)) {
                        a.append(" ").append(value);
                        break;
                    }
                }
            }
            System.out.println(a.toString());
        }
    }

    /**
     * 选择排序
     */
    @Test
    public void selectSort() {
        int[] arr = Myself.INT_ARR;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        System.out.println(JSONUtil.toJsonStr(arr));
    }

    /**
     * 冒泡排序是前后两两值进行交换
     * if(a>b){
     * int c = a;
     * a = b;
     * b = c;
     * }
     */
    @Test
    public void bubbleSort() {
        int[] arr = Myself.INT_ARR;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        System.out.println(JSONUtil.toJsonStr(arr));
    }

}



