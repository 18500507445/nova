package com.nova.book.effectivejava.chapter1.section5;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/13 16:52
 */
class Section5Test {

    private static final Date DATE_ONE = new Date(1990, Calendar.JANUARY, 0);
    private static final Date DATE_TWO = new Date(2000, Calendar.JANUARY, 0);

    /**
     * 一般都不会这么写
     */
    @Test
    public void demoA() {
        String str = new String("abc");
        System.out.println("str = " + str);
    }

    /**
     * 避免频繁拆箱装箱
     */
    @Test
    public void demoB() {
        Integer num = 100;
        for (Integer i = 0; i < num; i++) {
            i++;
            System.out.println("i = " + i);
        }
    }

    /**
     * 每一次请求进来都需要创建,改成静态常量
     */
    @Test
    public void demoC() {
        Date now = new Date();
        for (int i = 0; i < 100; i++) {
            Date date90 = new Date(1990, Calendar.JANUARY, 0);
            Date date00 = new Date(2000, Calendar.JANUARY, 0);
            if (now.after(date90) && now.before(date00)) {
                System.out.println("true = " + true);
            }
        }

        for (int i = 0; i < 100; i++) {
            if (now.after(DATE_ONE) && now.before(DATE_TWO)) {
                System.out.println("true = " + true);
            }
        }
    }

}
