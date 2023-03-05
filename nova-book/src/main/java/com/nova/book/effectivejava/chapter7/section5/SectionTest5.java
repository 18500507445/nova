package com.nova.book.effectivejava.chapter7.section5;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest5 {

    /**
     * 空指针
     */
    @Test
    public void demoA() {
        Integer integer = null;
        System.out.println(integer == 13);
    }


    /**
     * 如果long sum改成包装类
     * sum+=i 就相当于 long sum1 = sum.longValue + x; sum = Long.valueOf(sum1)
     * <p>
     * 测试结果 性能差4-5倍
     */
    @Test
    public void demoB() {
        TimeInterval timer = DateUtil.timer();
        long sum = 0L;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }
        System.out.println(sum);
        System.out.println("耗时:" + timer.interval());
    }

}
