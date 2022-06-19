package com.nova.tools.demo.datatype;


import com.nova.tools.demo.entity.Myself;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author wangzehui
 * @date 2018/7/17 15:12
 */

public class BigDecimalExercise {

    public static void main(String[] args) {
        modularOperation();
    }

    /**
     * 商，余。
     */
    private static void modularOperation() {
        BigDecimal ten = new BigDecimal(10);
        BigDecimal[] bigDecimals = ten.divideAndRemainder(new BigDecimal(3));
        System.out.println("商:" + bigDecimals[0] + ",余数:" + bigDecimals[1]);
    }

    /**
     * 格式化
     */
    private static void format() {
        //格式化取小数点2位
        DecimalFormat df = new DecimalFormat("#.00");

        BigDecimal bigDecimal = new BigDecimal(-31.0121211);

        //舍0
        String s1 = bigDecimal.stripTrailingZeros().toPlainString();
        System.out.println("舍0:" + s1);

        String s = df.format(bigDecimal);

        //转double类型
        double i = Double.valueOf(s);
        double v = Double.parseDouble(s);

        //取绝对值
        System.out.println("取绝对值:" + Math.abs(i));
        System.out.println("取绝对值:" + Math.abs(v));
    }


    /**
     * 和0做校验并且末尾舍弃多余0
     *
     * @param price
     * @return
     */
    private String checkZero(BigDecimal price) {
        if (price.compareTo(Myself.ZERO) <= 0) {
            price = Myself.ZERO;
        }
        return price.stripTrailingZeros().toPlainString();
    }
}



