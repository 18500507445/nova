package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Set;

/**
 * {@link NumberUtil} 单元测试类
 *
 * @author Looly
 */
public class NumberUtilTest {

    @Test
    public void addTest() {
        final Float a = 3.15f;
        final Double b = 4.22;
        final double result = NumberUtil.add(a, b).doubleValue();
        Assert.equals(7.37, result);
    }

    @Test
    public void addTest2() {
        final double a = 3.15f;//精度丢失
        final double b = 4.22;
        final double result = NumberUtil.add(a, b);
        Assert.equals(7.37, result);
    }

    @Test
    public void addTest3() {
        final float a = 3.15f;
        final double b = 4.22;
        final double result = NumberUtil.add(a, b, a, b).doubleValue();
        Assert.equals(14.74, result);
    }

    @Test
    public void addTest4() {
        final BigDecimal result = NumberUtil.add(new BigDecimal("133"), new BigDecimal("331"));
        Assert.equals(new BigDecimal("464"), result);
    }

    @Test
    public void addBlankTest() {
        final BigDecimal result = NumberUtil.add("123", " ");
        Assert.equals(new BigDecimal("123"), result);
    }

    @Test
    public void isIntegerTest() {
        Assert.isTrue(NumberUtil.isInteger("-12"));
        Assert.isTrue(NumberUtil.isInteger("256"));
        Assert.isTrue(NumberUtil.isInteger("0256"));
        Assert.isTrue(NumberUtil.isInteger("0"));
        Assert.isFalse(NumberUtil.isInteger("23.4"));
        Assert.isFalse(NumberUtil.isInteger(null));
        Assert.isFalse(NumberUtil.isInteger(""));
        Assert.isFalse(NumberUtil.isInteger(" "));
    }

    @Test
    public void isLongTest() {
        Assert.isTrue(NumberUtil.isLong("-12"));
        Assert.isTrue(NumberUtil.isLong("256"));
        Assert.isTrue(NumberUtil.isLong("0256"));
        Assert.isTrue(NumberUtil.isLong("0"));
        Assert.isFalse(NumberUtil.isLong("23.4"));
        Assert.isFalse(NumberUtil.isLong(null));
        Assert.isFalse(NumberUtil.isLong(""));
        Assert.isFalse(NumberUtil.isLong(" "));
    }

    @Test
    public void isNumberTest() {
        Assert.isTrue(NumberUtil.isNumber("28.55"));
        Assert.isTrue(NumberUtil.isNumber("0"));
        Assert.isTrue(NumberUtil.isNumber("+100.10"));
        Assert.isTrue(NumberUtil.isNumber("-22.022"));
        Assert.isTrue(NumberUtil.isNumber("0X22"));
    }

    @Test
    public void divTest() {
        final double result = NumberUtil.div(0, 1);
        Assert.equals(0.0, result);
    }

    @Test
    public void divBigDecimalTest() {
        final BigDecimal result = NumberUtil.div(BigDecimal.ZERO, BigDecimal.ONE);
        Assert.equals(BigDecimal.ZERO, result.stripTrailingZeros());
    }

    @Test
    public void roundTest() {

        // 四舍
        final String round1 = NumberUtil.roundStr(2.674, 2);
        final String round2 = NumberUtil.roundStr("2.674", 2);
        Assert.equals("2.67", round1);
        Assert.equals("2.67", round2);

        // 五入
        final String round3 = NumberUtil.roundStr(2.675, 2);
        final String round4 = NumberUtil.roundStr("2.675", 2);
        Assert.equals("2.68", round3);
        Assert.equals("2.68", round4);

        // 四舍六入五成双
        final String round31 = NumberUtil.roundStr(4.245, 2, RoundingMode.HALF_EVEN);
        final String round41 = NumberUtil.roundStr("4.2451", 2, RoundingMode.HALF_EVEN);
        Assert.equals("4.24", round31);
        Assert.equals("4.25", round41);

        // 补0
        final String round5 = NumberUtil.roundStr(2.6005, 2);
        final String round6 = NumberUtil.roundStr("2.6005", 2);
        Assert.equals("2.60", round5);
        Assert.equals("2.60", round6);

        // 补0
        final String round7 = NumberUtil.roundStr(2.600, 2);
        final String round8 = NumberUtil.roundStr("2.600", 2);
        Assert.equals("2.60", round7);
        Assert.equals("2.60", round8);
    }

    @Test
    public void roundStrTest() {
        final String roundStr = NumberUtil.roundStr(2.647, 2);
        Assert.equals(roundStr, "2.65");

        final String roundStr1 = NumberUtil.roundStr(0, 10);
        Assert.equals(roundStr1, "0.0000000000");
    }

    @Test
    public void roundHalfEvenTest() {
        String roundStr = NumberUtil.roundHalfEven(4.245, 2).toString();
        Assert.equals(roundStr, "4.24");
        roundStr = NumberUtil.roundHalfEven(4.2450, 2).toString();
        Assert.equals(roundStr, "4.24");
        roundStr = NumberUtil.roundHalfEven(4.2451, 2).toString();
        Assert.equals(roundStr, "4.25");
        roundStr = NumberUtil.roundHalfEven(4.2250, 2).toString();
        Assert.equals(roundStr, "4.22");

        roundStr = NumberUtil.roundHalfEven(1.2050, 2).toString();
        Assert.equals(roundStr, "1.20");
        roundStr = NumberUtil.roundHalfEven(1.2150, 2).toString();
        Assert.equals(roundStr, "1.22");
        roundStr = NumberUtil.roundHalfEven(1.2250, 2).toString();
        Assert.equals(roundStr, "1.22");
        roundStr = NumberUtil.roundHalfEven(1.2350, 2).toString();
        Assert.equals(roundStr, "1.24");
        roundStr = NumberUtil.roundHalfEven(1.2450, 2).toString();
        Assert.equals(roundStr, "1.24");
        roundStr = NumberUtil.roundHalfEven(1.2550, 2).toString();
        Assert.equals(roundStr, "1.26");
        roundStr = NumberUtil.roundHalfEven(1.2650, 2).toString();
        Assert.equals(roundStr, "1.26");
        roundStr = NumberUtil.roundHalfEven(1.2750, 2).toString();
        Assert.equals(roundStr, "1.28");
        roundStr = NumberUtil.roundHalfEven(1.2850, 2).toString();
        Assert.equals(roundStr, "1.28");
        roundStr = NumberUtil.roundHalfEven(1.2950, 2).toString();
        Assert.equals(roundStr, "1.30");
    }

    @Test
    public void decimalFormatTest() {
        final long c = 299792458;// 光速

        final String format = NumberUtil.decimalFormat(",###", c);
        Assert.equals("299,792,458", format);
    }

    @Test
    public void decimalFormatNaNTest() {
        final Double a = 0D;
        final Double b = 0D;

        final Double c = a / b;
        Console.log(NumberUtil.decimalFormat("#%", c));
    }

    @Test
    public void decimalFormatNaNTest2() {
        final Double a = 0D;
        final Double b = 0D;

        Console.log(NumberUtil.decimalFormat("#%", a / b));
    }

    @Test
    public void decimalFormatDoubleTest() {
        final Double c = 467.8101;

        final String format = NumberUtil.decimalFormat("0.00", c);
        Assert.equals("467.81", format);
    }

    @Test
    public void decimalFormatMoneyTest() {
        final double c = 299792400.543534534;

        final String format = NumberUtil.decimalFormatMoney(c);
        Assert.equals("299,792,400.54", format);

        final double value = 0.5;
        final String money = NumberUtil.decimalFormatMoney(value);
        Assert.equals("0.50", money);
    }

    @Test
    public void equalsTest() {
        Assert.isTrue(NumberUtil.equals(new BigDecimal("0.00"), BigDecimal.ZERO));
    }

    @Test
    public void toBigDecimalTest() {
        final double a = 3.14;

        BigDecimal bigDecimal = NumberUtil.toBigDecimal(a);
        Assert.equals("3.14", bigDecimal.toString());

        bigDecimal = NumberUtil.toBigDecimal("1,234.55");
        Assert.equals("1234.55", bigDecimal.toString());

        bigDecimal = NumberUtil.toBigDecimal("1,234.56D");
        Assert.equals("1234.56", bigDecimal.toString());
    }

    @Test
    public void maxTest() {
        final int max = NumberUtil.max(5, 4, 3, 6, 1);
        Assert.equals(6, max);
    }

    @Test
    public void minTest() {
        final int min = NumberUtil.min(5, 4, 3, 6, 1);
        Assert.equals(1, min);
    }

    @Test
    public void parseIntTest() {
        int number = NumberUtil.parseInt("0xFF");
        Assert.equals(255, number);

        // 0开头
        number = NumberUtil.parseInt("010");
        Assert.equals(10, number);

        number = NumberUtil.parseInt("10");
        Assert.equals(10, number);

        number = NumberUtil.parseInt("   ");
        Assert.equals(0, number);

        number = NumberUtil.parseInt("10F");
        Assert.equals(10, number);

        number = NumberUtil.parseInt("22.4D");
        Assert.equals(22, number);

        number = NumberUtil.parseInt("22.6D");
        Assert.equals(22, number);

        number = NumberUtil.parseInt("0");
        Assert.equals(0, number);

        number = NumberUtil.parseInt(".123");
        Assert.equals(0, number);
    }

    @Test
    public void parseIntTest2() {
        // from 5.4.8 issue#I23ORQ@Gitee
        // 千位分隔符去掉
        final int v1 = NumberUtil.parseInt("1,482.00");
        Assert.equals(1482, v1);
    }

    @Test
    public void parseIntTest3() {
        final int v1 = NumberUtil.parseInt("d");
        Assert.equals(0, v1);
    }

    @Test
    public void parseNumberTest4() {
        // issue#I5M55F
        // 科学计数法忽略支持，科学计数法一般用于表示非常小和非常大的数字，这类数字转换为int后精度丢失，没有意义。
        final String numberStr = "429900013E20220812163344551";
        NumberUtil.parseInt(numberStr);
    }

    @Test
    public void parseNumberTest() {
        // from 5.4.8 issue#I23ORQ@Gitee
        // 千位分隔符去掉
        final int v1 = NumberUtil.parseNumber("1,482.00").intValue();
        Assert.equals(1482, v1);

        final Number v2 = NumberUtil.parseNumber("1,482.00D");
        Assert.equals(1482L, v2.longValue());
    }

    @Test
    public void parseNumberTest2() {
        // issue#I5M55F
        final String numberStr = "429900013E20220812163344551";
        final Number number = NumberUtil.parseNumber(numberStr);
        Assert.notNull(number);
        Assert.isTrue(number instanceof BigDecimal);
    }

    @Test
    public void parseHexNumberTest() {
        // 千位分隔符去掉
        final int v1 = NumberUtil.parseNumber("0xff").intValue();
        Assert.equals(255, v1);
    }

    @Test
    public void parseLongTest() {
        long number = NumberUtil.parseLong("0xFF");
        Assert.equals(255, number);

        // 0开头
        number = NumberUtil.parseLong("010");
        Assert.equals(10, number);

        number = NumberUtil.parseLong("10");
        Assert.equals(10, number);

        number = NumberUtil.parseLong("   ");
        Assert.equals(0, number);

        number = NumberUtil.parseLong("10F");
        Assert.equals(10, number);

        number = NumberUtil.parseLong("22.4D");
        Assert.equals(22, number);

        number = NumberUtil.parseLong("22.6D");
        Assert.equals(22, number);

        number = NumberUtil.parseLong("0");
        Assert.equals(0, number);

        number = NumberUtil.parseLong(".123");
        Assert.equals(0, number);
    }

    @Test
    public void factorialTest() {
        long factorial = NumberUtil.factorial(0);
        Assert.equals(1, factorial);

        Assert.equals(1L, NumberUtil.factorial(1));
        Assert.equals(1307674368000L, NumberUtil.factorial(15));
        Assert.equals(2432902008176640000L, NumberUtil.factorial(20));

        factorial = NumberUtil.factorial(5, 0);
        Assert.equals(120, factorial);
        factorial = NumberUtil.factorial(5, 1);
        Assert.equals(120, factorial);

        Assert.equals(5, NumberUtil.factorial(5, 4));
        Assert.equals(2432902008176640000L, NumberUtil.factorial(20, 0));
    }

    @Test
    public void factorialTest2() {
        long factorial = NumberUtil.factorial(new BigInteger("0")).longValue();
        Assert.equals(1, factorial);

        Assert.equals(1L, NumberUtil.factorial(new BigInteger("1")).longValue());
        Assert.equals(1307674368000L, NumberUtil.factorial(new BigInteger("15")).longValue());
        Assert.equals(2432902008176640000L, NumberUtil.factorial(20));

        factorial = NumberUtil.factorial(new BigInteger("5"), new BigInteger("0")).longValue();
        Assert.equals(120, factorial);
        factorial = NumberUtil.factorial(new BigInteger("5"), BigInteger.ONE).longValue();
        Assert.equals(120, factorial);

        Assert.equals(5, NumberUtil.factorial(new BigInteger("5"), new BigInteger("4")).longValue());
        Assert.equals(2432902008176640000L, NumberUtil.factorial(new BigInteger("20"), BigInteger.ZERO).longValue());
    }

    @Test
    public void mulTest() {
        final BigDecimal mul = NumberUtil.mul(new BigDecimal("10"), null);
        Assert.equals(BigDecimal.ZERO, mul);
    }


    @Test
    public void isPowerOfTwoTest() {
        Assert.isFalse(NumberUtil.isPowerOfTwo(-1));
        Assert.isTrue(NumberUtil.isPowerOfTwo(16));
        Assert.isTrue(NumberUtil.isPowerOfTwo(65536));
        Assert.isTrue(NumberUtil.isPowerOfTwo(1));
        Assert.isFalse(NumberUtil.isPowerOfTwo(17));
    }

    @Test
    public void generateRandomNumberTest() {
        final int[] ints = NumberUtil.generateRandomNumber(10, 20, 5);
        Assert.equals(5, ints.length);
        final Set<?> set = Convert.convert(Set.class, ints);
        Assert.equals(5, set.size());
    }

    @Test
    public void toStrTest() {
        Assert.equals("1", NumberUtil.toStr(new BigDecimal("1.0000000000")));
        Assert.equals("0", NumberUtil.toStr(NumberUtil.sub(new BigDecimal("9600.00000"), new BigDecimal("9600.00000"))));
        Assert.equals("0", NumberUtil.toStr(NumberUtil.sub(new BigDecimal("9600.0000000000"), new BigDecimal("9600.000000"))));
        Assert.equals("0", NumberUtil.toStr(new BigDecimal("9600.00000").subtract(new BigDecimal("9600.000000000"))));
    }

    @Test
    public void generateRandomNumberTest2() {
        // 检查边界
        final int[] ints = NumberUtil.generateRandomNumber(1, 8, 7);
        Assert.equals(7, ints.length);
        final Set<?> set = Convert.convert(Set.class, ints);
        Assert.equals(7, set.size());
    }

    @Test
    public void toPlainNumberTest() {
        final String num = "5344.34234e3";
        final String s = new BigDecimal(num).toPlainString();
        Assert.equals("5344342.34", s);
    }

    @Test
    public void generateBySetTest() {
        final Integer[] integers = NumberUtil.generateBySet(10, 100, 5);
        Assert.equals(5, integers.length);
    }

    @Test
    public void isOddOrEvenTest() {
        final int[] a = {0, 32, -32, 123, -123};
        Assert.isFalse(NumberUtil.isOdd(a[0]));
        Assert.isTrue(NumberUtil.isEven(a[0]));

        Assert.isFalse(NumberUtil.isOdd(a[1]));
        Assert.isTrue(NumberUtil.isEven(a[1]));

        Assert.isFalse(NumberUtil.isOdd(a[2]));
        Assert.isTrue(NumberUtil.isEven(a[2]));

        Assert.isTrue(NumberUtil.isOdd(a[3]));
        Assert.isFalse(NumberUtil.isEven(a[3]));

        Assert.isTrue(NumberUtil.isOdd(a[4]));
        Assert.isFalse(NumberUtil.isEven(a[4]));
    }

    @Test
    public void divIntegerTest() {
        Assert.equals(1001013, NumberUtil.div(100101300, (Number) 100).intValue());
    }

    @Test
    public void isDoubleTest() {
        Assert.isFalse(NumberUtil.isDouble(null));
        Assert.isFalse(NumberUtil.isDouble(""));
        Assert.isFalse(NumberUtil.isDouble("  "));
    }

    @Test
    public void range() {
        Assert.isFalse(NumberUtil.isIn(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("12")));
        Assert.isTrue(NumberUtil.isIn(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("2")));
        Assert.isTrue(NumberUtil.isIn(new BigDecimal("1"), new BigDecimal("0"), new BigDecimal("2")));
        Assert.isFalse(NumberUtil.isIn(new BigDecimal("0.23"), new BigDecimal("0.12"), new BigDecimal("0.22")));
        Assert.isTrue(NumberUtil.isIn(new BigDecimal("-0.12"), new BigDecimal("-0.3"), new BigDecimal("0")));
    }
}
