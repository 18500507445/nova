package com.nova.common.utils.list;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangzehui
 * @date 2018/9/20 20:10
 */

public class SplitListUtils {

    private static final Integer MAX_NUMBER = 2;

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        int limit = countStep(list.size());
        cutList(list, limit);

        List<List<Integer>> partition = Lists.partition(list, 2);
        System.out.println(JSONObject.toJSONString(partition));
    }

    private static void cutList(List<Integer> list, int limit) {
        //方法一：使用流遍历操作
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
            List<Integer> collect = list.stream().skip(i * MAX_NUMBER).limit(MAX_NUMBER).collect(Collectors.toList());
            System.out.println(collect);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 计算切分次数
     */
    private static Integer countStep(Integer size) {
        return (size + MAX_NUMBER - 1) / MAX_NUMBER;
    }


    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        if (n <= 0) {
            n = 1;
        }
        List<List<T>> result = new ArrayList<>();
        //(先计算出余数)
        int remaider = source.size() % n;
        //然后是商
        int number = source.size() / n;
        //偏移量
        int offset = 0;
        for (int i = 0; i < n; i++) {
            List<T> value;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }


}
