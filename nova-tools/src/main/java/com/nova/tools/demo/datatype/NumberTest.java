package com.nova.tools.demo.datatype;

import com.nova.tools.demo.entity.Myself;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @Description: 数字运算demo
 * @Author: wangzehui
 * @Date: 2022/11/17 20:15
 */
public class NumberTest {

    /**
     * 测试float
     */
    @Test
    public void floatDemo(){
        System.out.println(0.9f - 0.8f);
        System.out.println(1f - 0.7f);
        System.out.println("1" + "2");
    }


    /**
     * 测试double
     */
    @Test
    public void doubleDemo() {
        double m = 1.01;
        double n = 365 * 1;
        //m的n次幂
        Double pow = Math.pow(m, n);
        //Double转long类型
        long l = pow.longValue();
        System.out.println(l);
    }

    /**
     * 商，余。
     */
    @Test
    public void modularOperation() {
        BigDecimal ten = new BigDecimal(10);
        BigDecimal[] bigDecimals = ten.divideAndRemainder(new BigDecimal(3));
        System.out.println("商:" + bigDecimals[0] + ",余数:" + bigDecimals[1]);
    }

    /**
     * 格式化
     */
    @Test
    public void format() {
        //格式化取小数点2位
        DecimalFormat df = new DecimalFormat("#.00");

        BigDecimal bigDecimal = BigDecimal.valueOf(-31.0121211);

        //舍0
        String s1 = bigDecimal.stripTrailingZeros().toPlainString();
        System.out.println("舍0:" + s1);

        String s = df.format(bigDecimal);

        //转double类型
        double i = Double.parseDouble(s);
        double v = Double.parseDouble(s);

        //取绝对值
        System.out.println("取绝对值:" + Math.abs(i));
        System.out.println("取绝对值:" + Math.abs(v));
    }

    /**
     * 和0做校验并且末尾舍弃多余0
     */
    @Test
    public void checkZero() {
        BigDecimal price = new BigDecimal("12.10");
        if (price.compareTo(Myself.ZERO) <= 0) {
            price = Myself.ZERO;
        }
        String s = price.stripTrailingZeros().toPlainString();

        System.out.println(price);
        System.out.println(s);
    }


}
