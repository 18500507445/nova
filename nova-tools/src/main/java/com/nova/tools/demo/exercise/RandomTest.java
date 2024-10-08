package com.nova.tools.demo.exercise;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.StrUtil;
import com.nova.common.utils.random.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 随机测试类
 * @author: wzh
 * @date: 2023/1/21 20:43
 */
class RandomTest {

    public static int sum1, sum2, sum3 = 0;

    /**
     * 随机方式一
     */
    @Test
    public void demoA() {
        Map<Object, Long> map = new HashMap<>(16);
        map.put("A", 1L);
        map.put("B", 1L);
        map.put("C", 1L);

        for (int i = 0; i < 1000; i++) {
            String random = RandomUtils.random(map).toString();
            if (StrUtil.equals("A", random)) {
                sum1++;
            }
            if (StrUtil.equals("B", random)) {
                sum2++;
            }
            if (StrUtil.equals("C", random)) {
                sum3++;
            }
            //System.err.println(random);
        }
        System.err.println("sum1:" + sum1);
        System.err.println("sum2:" + sum2);
        System.err.println("sum3:" + sum3);
    }

    /**
     * 方式二 参照huTool WeightRandom
     */
    @Test
    public void demoB() {
        WeightRandom<String> random = WeightRandom.create();
        random.add("A", 1);
        random.add("B", 1);
        random.add("C", 1);
        for (int i = 0; i < 1000; i++) {
            String result = random.next();

            if (StrUtil.equals("A", result)) {
                sum1++;
            }
            if (StrUtil.equals("B", result)) {
                sum2++;
            }
            if (StrUtil.equals("C", result)) {
                sum3++;
            }
        }
        System.err.println("sum1:" + sum1);
        System.err.println("sum2:" + sum2);
        System.err.println("sum3:" + sum3);

    }

}
