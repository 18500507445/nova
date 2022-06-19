package com.nova.tools.letcode;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2021/2/25 10:42
 */

public class ArrayTest {

    public static void main(String[] args) {
        List<Boolean> booleans = kidsWithCandies();
        System.out.println(JSONObject.toJSONString(booleans));
    }


    /**
     * 给你一个整数数组 nums 。
     * 如果一组数字 (i,j) 满足 nums[i] == nums[j] 且 i < j ，就可以认为这是一组 好数对 。
     * 返回好数对的数目。
     *
     * @return
     */
    public static int numIdenticalPairs() {
        int[] nums = {1, 2, 3, 1, 1, 3};
        int a = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 1; j < nums.length; j++) {
                if (nums[i] == nums[j] && i < j) {
                    a++;
                }
            }
        }
        return a;
    }

    /**
     * 给你一个 m x n 的整数网格 accounts ，其中 accounts[i][j] 是第 i​​​​​​​​​​​​ 位客户在第 j 家银行托管的资产数量。返回最富有客户所拥有的 资产总量 。
     * 客户的 资产总量 就是他们在各家银行托管的资产数量之和。最富有客户就是 资产总量 最大的客户。
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/richest-customer-wealth
     *
     * @return
     */
    public static int maximumWealth() {
        int[][] nums = {{4, 5, 6, 8}, {2, 3}, {1, 6, 9}};
        int max = 0;
        for (int[] num : nums) {
            int sum = 0;
            for (int i : num) {
                sum = sum + i;
            }
            if (sum > max) {
                max = sum;
            }
        }

        /**
         * 方法二
         */
        Integer integer = Arrays.stream(nums).map(ints -> Arrays.stream(ints).sum()).max(Integer::compareTo).get();
        System.out.println(integer);

        return max;
    }

    /**
     * 给你一个数组 candies 和一个整数 extraCandies ，其中 candies[i] 代表第 i 个孩子拥有的糖果数目。
     * 对每一个孩子，检查是否存在一种方案，将额外的 extraCandies 个糖果分配给孩子们之后，此孩子有 最多 的糖果。注意，允许有多个孩子同时拥有 最多 的糖果数目。
     */
    public static List<Boolean> kidsWithCandies() {
        int[] candies = {2, 3, 5, 1, 3};
        int extraCandies = 3;
        int max = 0;
        List<Boolean> booleans = new ArrayList<>();
        for (int candy : candies) {
            if (candy > max) {
                max = candy;
            }
        }
        for (int candy : candies) {
            booleans.add(candy + extraCandies >= max);
        }
        return booleans;
    }
}
