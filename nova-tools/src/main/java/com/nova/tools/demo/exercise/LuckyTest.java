package com.nova.tools.demo.exercise;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wzh
 * @description: 抽奖系统设计
 * @date: 2024/10/18 15:21
 */
@Slf4j(topic = "Lucky")
public class LuckyTest {

    public static void main(String[] args) {
        //1. 构造Map<goodsId，Map<概率，中奖号码>>
        List<Map<String, Map<BigDecimal, List<Integer>>>> goodsList = new ArrayList<>();
        Map<String, Map<BigDecimal, List<Integer>>> one = new HashMap<>();
        one.put("一等奖", MapUtil.of(new BigDecimal("0.01"), new ArrayList<>()));
        Map<String, Map<BigDecimal, List<Integer>>> two = new HashMap<>();
        two.put("二等奖", MapUtil.of(new BigDecimal("0.05"), new ArrayList<>()));
        Map<String, Map<BigDecimal, List<Integer>>> three = new HashMap<>();
        three.put("三等奖", MapUtil.of(new BigDecimal("0.3"), new ArrayList<>()));
        goodsList.add(one);
        goodsList.add(two);
        goodsList.add(three);

        //2. 计算出总奖池数
        List<BigDecimal> luckyList = new ArrayList<>();
        for (Map<String, Map<BigDecimal, List<Integer>>> hashMap : goodsList) {
            Map<BigDecimal, List<Integer>> value = hashMap.entrySet().iterator().next().getValue();
            BigDecimal rate = value.entrySet().iterator().next().getKey();
            luckyList.add(rate);
        }
        String min = luckyList.stream().min(BigDecimal::compareTo).get().toPlainString();
        int decimalPlaces = min.length() - min.indexOf('.') - 1;
        Console.error("小数点 {} 位 ", decimalPlaces);

        // 计算输出奖池数
        int result = (int) Math.pow(10, decimalPlaces);

        Console.error("奖池数", result);

        //3. 分配概率里面的中奖号码
        int begin = 1;
        for (Map<String, Map<BigDecimal, List<Integer>>> hashMap : goodsList) {
            String key = hashMap.entrySet().iterator().next().getKey();
            Map<BigDecimal, List<Integer>> value = hashMap.entrySet().iterator().next().getValue();
            BigDecimal bigDecimal = value.entrySet().iterator().next().getKey();
            List<Integer> numberList = value.entrySet().iterator().next().getValue();
            int number = NumberUtil.mul(result, bigDecimal).intValue();
            for (int i = 0; i < number; i++) {
                numberList.add(begin);
                begin++;
            }
            Console.error("{}，中奖概率：{}，奖池号码：{}", key, bigDecimal, numberList);
        }

        //4. 开始抽奖
        TimeInterval timer = DateUtil.timer();
        for (int i = 1; i <= 100; i++) {
            int number = RandomUtil.randomInt(1, result);
            for (Map<String, Map<BigDecimal, List<Integer>>> hashMap : goodsList) {
                String key = hashMap.entrySet().iterator().next().getKey();
                Map<BigDecimal, List<Integer>> value = hashMap.entrySet().iterator().next().getValue();
                List<Integer> numberList = value.entrySet().iterator().next().getValue();
                if (numberList.contains(number)) {
                    Console.error("恭喜用户：{} 中奖，号码：{}，奖品：{}", i, number, key);
                } else {
                    Console.error("用户：{} ，随机号码：{}", i, number);
                }
            }
        }
        Console.error("抽奖结束 耗时：{} ms", timer.interval());
    }

}
