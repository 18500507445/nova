package com.nova.tools.leetcode.base;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 从数组找出 两数之和等于一个目标数 返回下标
 * @author: wzh
 * @date: 2020/6/16 13:38
 */

public class TwoSum {
    private static int[] nums = {2, 6, 7, 4, 10, 8};

    private static int target = 8;

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(16);
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    public static void main(String[] args) {
        int[] ints = twoSum(nums, target);
        for (int anInt : ints) {
            System.out.println(anInt);
        }

//        List<Integer> integers = new ArrayList<>();
//        for (int i = 0; i < nums.length - 1; i++) {
//            if (nums[i] + nums[i + 1] == target) {
//                integers.add(i);
//                integers.add(i + 1);
//            }
//        }
//        System.out.println(JSONObject.toJSONString(integers));

    }

}
