package com.nova.tools.leetcode;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        String target = "III";
        //从第二个元素开始
        char[] charArray = target.toCharArray();
        Integer pre = hashMap.get(charArray[0]);
        int sum = 0;
        for (int i = 1; i < charArray.length; i++) {
            Integer num = hashMap.get(charArray[i]);
            if (pre < num) {
                sum -= pre;
            } else {
                sum += pre;
            }
            pre = num;
        }
        //把最后一位加进去
        sum += pre;
        System.err.println("sum = " + sum);
    }


    //最长公共前缀
    @Test
    public void topic14() {
        String[] arr = {"reflower", "flow", "flight"};
        List<String> list = new ArrayList<>();
        int num;
        StringBuilder sb = new StringBuilder();
        char[] first = arr[0].toCharArray();
        for (char c : first) {
            sb.append(c);
            num = 0;
            for (String s : arr) {
                if (s.contains(sb) && s.indexOf(String.valueOf(sb)) == 0) {
                    num++;
                    if (num >= arr.length) {
                        list.add(sb.toString());
                        break;
                    }
                }
            }
        }
        String s = list.isEmpty() ? "" : list.get(list.size() - 1);
        System.out.println("s = " + s);
    }

    //有效的括号
    @Test
    public void topic20() {
        String s = "({[]})";
        int len = s.length() / 2;
        for (int i = 0; i < len; i++) {
            s = s.replace("()", "").replace("{}", "").replace("[]", "");
        }
        System.err.println("result = " + s.isEmpty());
    }


    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "val=" + val +
                    ", next=" + next +
                    '}';
        }
    }

    //合并两个有序链表
    @Test
    public void topic21() {
        ListNode newList = null;
        ListNode list1 = new ListNode(1, new ListNode(3));
        ListNode list2 = new ListNode(2, new ListNode(4));

        int val1 = list1.val;
        int val2 = list2.val;
        if (val1 < val2) {
            ListNode next1 = list1.next;
            ListNode next2 = list2.next;

            ListNode n1 = new ListNode(val2);
            n1.next = next1;
            next1.next = next2;
            newList = new ListNode(val1, n1);
        }
        System.out.println("newList = " + newList);
    }

}
