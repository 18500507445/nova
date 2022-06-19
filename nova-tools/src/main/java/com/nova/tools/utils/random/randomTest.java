package com.nova.tools.utils.random;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/** 微信红包算法 https://wenku.baidu.com/view/a15fee9cfe0a79563c1ec5da50e2524de518d092.html
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/6/17 13:32
 */
public class randomTest {


    /**
     * 根据权重获取序号
     *
     * @param weights
     * @return
     */
    public static int randomByWeight(int[][] weights) {
        Random random = new Random();
        int weightSum = 0;
        //注意这一步至关重要 10000是精度 这个值越大精度越高 这里取10的倍数 因为比例不变
        for (int[] weight : weights) {
            weightSum += weight[1] * 10000;
        }
        //总的权重中生成一个随机数
        int numberRand = random.nextInt(weightSum);
        int sumTemp = 0;
        //循环数组 判断生成的随机数是否小于等于当前权重是就返回序号 否则加上当前的权重继续循环
        for (int[] weight : weights) {
            sumTemp += weight[1] * 10000;
            if (numberRand <= sumTemp) {
                return weight[0];
            }
        }
        return -1;
    }

    public static int random(Map<String, String> map) {
        Set<Map.Entry<String, String>> entries = map.entrySet();
        Random random = new Random();
        int sum = 0;
        for (Map.Entry<String, String> entry : entries) {
            String value = entry.getValue();
            int valueInt = Integer.parseInt(value);
            sum += valueInt;
        }
        int randomInt = random.nextInt(sum) + 1;
        int sumTemp = 0;
        for (Map.Entry<String, String> entry : entries) {
            String value = entry.getValue();
            int valueInt = Integer.parseInt(value);
            sumTemp += valueInt;
            if (randomInt <= sumTemp) {
                return Integer.parseInt(entry.getKey());
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>(16);
        map.put("1", "5");
        map.put("2", "2");
        map.put("3", "2");
        map.put("4", "2");
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        for (int i = 0; i < 100000; i++) {
            int random = random(map);
            if (random == 1) {
                sum1++;
            }
            if (random == 2) {
                sum2++;
            }
            if (random == 3) {
                sum3++;
            }
            if (random == 4) {
                sum4++;
            }
        }
        System.out.println("sum1:" + sum1);
        System.out.println("sum2:" + sum2);
        System.out.println("sum3:" + sum3);
        System.out.println("sum4:" + sum4);

    }


}
