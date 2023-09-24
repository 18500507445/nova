package com.nova.tools.demo.exercise;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * @description: 红包算法模拟
 * @see <a href="https://https://wenku.baidu.com/view/a15fee9cfe0a79563c1ec5da50e2524de518d092.html</a>
 * @author: wzh
 * @date: 2023/1/21 20:44
 */
class RedPackageTest {

    /**
     * 剩余金额随机法
     * 算法一是不推荐使用的，听名字就知道这个方法是将剩余的金额进行随机分配
     *
     * @param amount
     * @param min
     * @param num
     */
    public static void function1(BigDecimal amount, BigDecimal min, BigDecimal num) {
        BigDecimal remain = amount.subtract(min.multiply(num));
        final Random random = new Random();
        final BigDecimal hundred = new BigDecimal("100");
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal redPack;
        for (int i = 0; i < num.intValue(); i++) {
            final int nextInt = random.nextInt(100);
            if (i == num.intValue() - 1) {
                redPack = remain;
            } else {
                redPack = new BigDecimal(nextInt).multiply(remain).divide(hundred, 2, RoundingMode.FLOOR);
            }
            if (remain.compareTo(redPack) > 0) {
                remain = remain.subtract(redPack);
            } else {
                remain = BigDecimal.ZERO;
            }
            sum = sum.add(min.add(redPack));
            System.err.println("第" + (i + 1) + "个人抢到红包金额为：" + min.add(redPack).setScale(2, RoundingMode.HALF_UP));
        }
        System.err.println("红包总额：" + sum.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * 整体随机法
     * 整体金额随机法的公式：红包总额 * 随机数/随机数总和，这个方法的核心是使用一个随机数作为红包瓜分的标准，
     * 这个随机数是通过Random类随机产生的。他的随机性就比较大了，看起来好像是和我们平时抢红包差不多，
     * 但是微信红包也不是采用这种方法，因为这种的随机性太大了，不是很公平。
     *
     * @param amount
     * @param min
     * @param num
     */
    private static void function2(BigDecimal amount, BigDecimal min, BigDecimal num) {
        final Random random = new Random();
        final int[] rand = new int[num.intValue()];
        BigDecimal sum1 = BigDecimal.ZERO;
        BigDecimal redpeck;
        int sum = 0;
        for (int i = 0; i < num.intValue(); i++) {
            rand[i] = random.nextInt(100);
            sum += rand[i];
        }
        final BigDecimal bigDecimal = new BigDecimal(sum);
        BigDecimal remain = amount.subtract(min.multiply(num));
        for (int i = 0; i < rand.length; i++) {
            if (i == num.intValue() - 1) {
                redpeck = remain;
            } else {
                redpeck = remain.multiply(new BigDecimal(rand[i])).divide(bigDecimal, 2, RoundingMode.FLOOR);
            }
            if (remain.compareTo(redpeck) > 0) {
                remain = remain.subtract(redpeck);
            } else {
                remain = BigDecimal.ZERO;
            }
            sum1 = sum1.add(min.add(redpeck)).setScale(2, RoundingMode.HALF_UP);
            System.err.println("第" + (i + 1) + "个人抢到红包金额为：" + min.add(redpeck).setScale(2, RoundingMode.HALF_UP));
        }

        System.err.println("红包总额：" + sum1);
    }

    /**
     * 割线法
     * 割线法指的是把红包总金额想象成一条很长的线段，而每个人抢到的金额，则是这条主线段所拆分出的若干子线段，当所有切割点确定以后，子线段的长度也随之确定
     * 这样每个人来抢红包的时候，只需要顺次领取与子线段长度等价的红包金额即可。
     *
     * @param amount
     * @param min
     * @param num
     */
    public static void function3(BigDecimal amount, BigDecimal min, BigDecimal num) {
        final Random random = new Random();
        final int[] rand = new int[num.intValue()];
        BigDecimal sum1 = BigDecimal.ZERO;
        BigDecimal redpeck;
        int sum = 0;
        for (int i = 0; i < num.intValue(); i++) {
            rand[i] = random.nextInt(100);
            sum += rand[i];
        }
        final BigDecimal bigDecimal = new BigDecimal(sum);
        BigDecimal remain = amount.subtract(min.multiply(num));
        for (int i = 0; i < rand.length; i++) {
            if (i == num.intValue() - 1) {
                redpeck = remain;
            } else {
                redpeck = remain.multiply(new BigDecimal(rand[i]))
                        .divide(bigDecimal, 2, RoundingMode.FLOOR);
            }
            if (remain.compareTo(redpeck) > 0) {
                remain = remain.subtract(redpeck).setScale(2, RoundingMode.HALF_UP);
            } else {
                remain = BigDecimal.ZERO;
            }
            sum1 = sum1.add(min.add(redpeck).setScale(2, RoundingMode.HALF_UP));
            System.err.println("第" + (i + 1) + "个人抢到红包金额为：" + min.add(redpeck));
        }

        System.err.println("红包总额：" + sum1);
    }

    /**
     * 算法四就是微信红包目前所采用的的算法（大致思路，代码模拟），二倍均值计算公式：2 * 剩余金额/剩余红包数。
     */
    public static void function4(BigDecimal amount, BigDecimal min, BigDecimal num) {
        BigDecimal remain = amount.subtract(min.multiply(num));
        final Random random = new Random();
        final BigDecimal hundred = new BigDecimal("100");
        final BigDecimal two = new BigDecimal("2");
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal redpeck;
        for (int i = 0; i < num.intValue(); i++) {
            final int nextInt = random.nextInt(100);
            if (i == num.intValue() - 1) {
                redpeck = remain;
            } else {
                redpeck = new BigDecimal(nextInt).multiply(remain.multiply(two).divide(num.subtract(new BigDecimal(i)), 2, RoundingMode.CEILING)).divide(hundred, 2, RoundingMode.FLOOR);
            }
            if (remain.compareTo(redpeck) > 0) {
                remain = remain.subtract(redpeck).setScale(2, RoundingMode.HALF_UP);
            } else {
                remain = BigDecimal.ZERO;
            }
            sum = sum.add(min.add(redpeck)).setScale(2, RoundingMode.HALF_UP);
            System.err.println("第" + (i + 1) + "个人抢到红包金额为：" + min.add(redpeck));
        }
        System.err.println("红包总额：" + sum);
    }

    @Test
    public void demoA() {
        BigDecimal amount = new BigDecimal(100).setScale(2, RoundingMode.HALF_UP);
        BigDecimal min = new BigDecimal("0.01").setScale(2, RoundingMode.HALF_UP);
        BigDecimal num = new BigDecimal(10).setScale(2, RoundingMode.HALF_UP);
        function1(amount, min, num);
    }

    @Test
    public void demoB() {
        BigDecimal amount = new BigDecimal(100).setScale(2, RoundingMode.HALF_UP);
        BigDecimal min = new BigDecimal("0.01").setScale(2, RoundingMode.HALF_UP);
        BigDecimal num = new BigDecimal(10).setScale(2, RoundingMode.HALF_UP);
        function2(amount, min, num);
    }

    @Test
    public void demoC() {
        BigDecimal amount = new BigDecimal(100).setScale(2, RoundingMode.HALF_UP);
        BigDecimal min = new BigDecimal("0.01").setScale(2, RoundingMode.HALF_UP);
        BigDecimal num = new BigDecimal(10).setScale(2, RoundingMode.HALF_UP);
        function3(amount, min, num);
    }

    @Test
    public void demoD() {
        BigDecimal amount = new BigDecimal(100).setScale(2, RoundingMode.HALF_UP);
        BigDecimal min = new BigDecimal("0.01").setScale(2, RoundingMode.HALF_UP);
        BigDecimal num = new BigDecimal(10).setScale(2, RoundingMode.HALF_UP);
        function4(amount, min, num);
    }
}
