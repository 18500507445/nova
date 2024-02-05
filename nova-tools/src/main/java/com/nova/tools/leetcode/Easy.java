package com.nova.tools.leetcode;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wzh
 * @description: 简单题目
 * @date: 2024/02/05 16:18
 */
public class Easy {

    //两数之和
    @Test
    public void topic1() {
        int[] nums = {2, 7, 11, 15};
        int target = 17;

        for (int num : nums) {
            for (int j = 1; j < nums.length; j++) {
                if (num + nums[j] == target) {
                    System.out.println(num + " + " + nums[j] + " = " + target);
                }
            }
        }
    }

    //回文数
    @Test
    public void topic9() {
        int num = 12111;

        int a = num;
        int result = 0;
        while (num != 0) {
            result = result * 10 + num % 10;
            num = num / 10;
        }
        if (a == result) {
            System.out.println("回文");
        } else {
            System.out.println("不是回文");
        }
    }

    //罗马数字转整数
    @Test
    public void topic13() {
        Map<Character, Integer> hashMap = new HashMap<>(16);
        hashMap.put('I', 1);
        hashMap.put('V', 5);
        hashMap.put('X', 10);
        hashMap.put('L', 50);
        hashMap.put('C', 100);
        hashMap.put('D', 500);
        hashMap.put('M', 1000);

        String target = "LVIII";

        char[] charArray = target.toCharArray();
        int sum = 0;
        for (int i = 0; i < charArray.length; i++) {
            char now = charArray[i];
            if (i + 1 < charArray.length) {

            }
            char before = charArray[i + 1];
            System.out.println("now = " + now + "before = " + before);
        }
        System.err.println("sum = " + sum);
    }


}
