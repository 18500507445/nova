package com.nova.tools.demo.datatype;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.StrUtil;
import com.nova.common.utils.random.RandomUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/21 20:43
 */
public class RandomTest {

    /**
     * 方式二 参照huTool WeightRandom
     *
     * @param args
     */
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
            String random = RandomUtil.random(map).toString();
            if (StrUtil.equals("211439226724", random)) {
                sum1++;
            }
            if (StrUtil.equals("211439226042", random)) {
                sum2++;
            }
            if (StrUtil.equals("211439237059", random)) {
                sum3++;
            }
            if (StrUtil.equals("211439972426", random)) {
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

            if (StrUtil.equals("A", result)) {
                sum6++;
            }
            if (StrUtil.equals("B", result)) {
                sum7++;
            }
            if (StrUtil.equals("C", result)) {
                sum8++;
            }
        }
        System.out.println("sum6:" + sum6);
        System.out.println("sum7:" + sum7);
        System.out.println("sum8:" + sum8);
    }

}
