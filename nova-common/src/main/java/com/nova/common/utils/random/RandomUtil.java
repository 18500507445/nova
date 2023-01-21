package com.nova.common.utils.random;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 微信红包算法 https://wenku.baidu.com/view/a15fee9cfe0a79563c1ec5da50e2524de518d092.html
 *
 * @description:
 * @author: wzh
 * @date: 2022/6/17 13:32
 */
public class RandomUtil {

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

    public static Object random(Map<Object, Object> map) {
        Set<Map.Entry<Object, Object>> entries = map.entrySet();
        Random random = new Random();
        int sum = 0;
        for (Map.Entry<Object, Object> entry : entries) {
            int valueInt = Integer.parseInt(String.valueOf(entry.getValue()));
            sum += valueInt;
        }
        int randomInt = random.nextInt(sum) + 1;
        int sumTemp = 0;
        for (Map.Entry<Object, Object> entry : entries) {
            int valueInt = Integer.parseInt(String.valueOf(entry.getValue()));
            sumTemp += valueInt;
            if (randomInt <= sumTemp) {
                return entry.getKey();
            }
        }
        return "";
    }


}
