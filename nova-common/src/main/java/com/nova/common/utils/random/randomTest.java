package com.nova.common.utils.random;


import cn.hutool.core.lang.WeightRandom;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 微信红包算法 https://wenku.baidu.com/view/a15fee9cfe0a79563c1ec5da50e2524de518d092.html
 *
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

    public static void main(String[] args) {
        Map<Object, Object> map = new HashMap<>(16);
        map.put("211439226724", "2");
        map.put("211439226042", "2");
        map.put("211439237059", "2");
        map.put("211439972426", "2");
        map.put("211439226759", "2");
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        for (int i = 0; i < 1000; i++) {
            String random = random(map).toString();
            if (StringUtils.equals("211439226724", random)) {
                sum1++;
            }
            if (StringUtils.equals("211439226042", random)) {
                sum2++;
            }
            if (StringUtils.equals("211439237059", random)) {
                sum3++;
            }
            if (StringUtils.equals("211439972426", random)) {
                sum4++;
            }
            System.out.println(random);
        }
        System.out.println("sum1:" + sum1);
        System.out.println("sum2:" + sum2);
        System.out.println("sum3:" + sum3);
        System.out.println("sum4:" + sum4);

        WeightRandom<String> random = WeightRandom.create();
        random.add("A", 1);
        random.add("B", 1);
        random.add("C", 1);
        int sum6 = 0, sum7 = 0, sum8 = 0;
        for (int i = 0; i < 1000; i++) {
            String result = random.next();

            if (StringUtils.equals("A", result)) {
                sum6++;
            }
            if (StringUtils.equals("B", result)) {
                sum7++;
            }
            if (StringUtils.equals("C", result)) {
                sum8++;
            }
        }
        System.out.println("sum6:" + sum6);
        System.out.println("sum7:" + sum7);
        System.out.println("sum8:" + sum8);
    }


}
