package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.lang.Assert;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * {@link RandomUtil} 随机工具类
 *
 * @author
 */
public class RandomUtilTest {

    @Test
    public void randomEleSetTest() {
        Set<Integer> set = RandomUtil.randomEleSet(CollUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
        Assert.equals(set.size(), 2);
    }

    @Test
    public void randomElesTest() {
        List<Integer> result = RandomUtil.randomEles(CollUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
        Assert.equals(result.size(), 2);
    }

    @Test
    public void randomDoubleTest() {
        double randomDouble = RandomUtil.randomDouble(0, 1, 0, RoundingMode.HALF_UP);
        Assert.isTrue(randomDouble <= 1);
    }

    @Test
    @Ignore
    public void randomBooleanTest() {
        Console.log(RandomUtil.randomBoolean());
    }

    @Test
    public void randomNumberTest() {
        final char c = RandomUtil.randomNumber();
        Assert.isTrue(c <= '9');
    }

    @Test
    public void randomIntTest() {
        final int c = RandomUtil.randomInt(10, 100);
        Assert.isTrue(c >= 10 && c < 100);
    }

    @Test
    public void randomBytesTest() {
        final byte[] c = RandomUtil.randomBytes(10);
        Assert.notNull(c);
    }

    @Test
    public void randomChineseTest() {
        char c = RandomUtil.randomChinese();
        Assert.isTrue(c > 0);
    }

    @Test
    @Ignore
    public void randomStringWithoutStrTest() {
        for (int i = 0; i < 100; i++) {
            final String s = RandomUtil.randomStringWithoutStr(8, "0IPOL");
            System.err.println(s);
            for (char c : "0IPOL".toCharArray()) {
                Assert.isFalse(s.contains((String.valueOf(c).toLowerCase(Locale.ROOT))));
            }
        }
    }

    @Test
    public void intAndBytesLittleEndianTest() {
        // 测试 int 转小端序 byte 数组
        int int1 = RandomUtil.randomInt((Integer.MAX_VALUE));

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(int1);
        byte[] bytesIntFromBuffer = buffer.array();

        byte[] bytesInt = ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN);
        Assert.equals(bytesIntFromBuffer, bytesInt);

        int int2 = ByteUtil.bytesToInt(bytesInt, ByteOrder.LITTLE_ENDIAN);
        Assert.equals(int1, int2);

        byte[] bytesInt2 = ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN);
        int int3 = ByteUtil.bytesToInt(bytesInt2, ByteOrder.LITTLE_ENDIAN);
        Assert.equals(int1, int3);

        byte[] bytesInt3 = ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN);
        int int4 = ByteUtil.bytesToInt(bytesInt3, ByteOrder.LITTLE_ENDIAN);
        Assert.equals(int1, int4);
    }

    @Test
    public void intAndBytesBigEndianTest() {
        // 测试 int 转大端序 byte 数组
        int int2 = RandomUtil.randomInt(Integer.MAX_VALUE);

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(int2);
        byte[] bytesIntFromBuffer = buffer.array();

        byte[] bytesInt = ByteUtil.intToBytes(int2, ByteOrder.BIG_ENDIAN);
        Assert.equals(bytesIntFromBuffer, bytesInt);

        // 测试大端序 byte 数组转 int
        int int3 = ByteUtil.bytesToInt(bytesInt, ByteOrder.BIG_ENDIAN);
        Assert.equals(int2, int3);
    }

    @Test
    public void longAndBytesLittleEndianTest() {
        // 测试 long 转 byte 数组
        long long1 = RandomUtil.randomLong(Long.MAX_VALUE);

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(long1);
        byte[] bytesLongFromBuffer = buffer.array();

        byte[] bytesLong = ByteUtil.longToBytes(long1, ByteOrder.LITTLE_ENDIAN);
        Assert.equals(bytesLongFromBuffer, bytesLong);

        long long2 = ByteUtil.bytesToLong(bytesLong, ByteOrder.LITTLE_ENDIAN);
        Assert.equals(long1, long2);

        byte[] bytesLong2 = ByteUtil.longToBytes(long1);
        long long3 = ByteUtil.bytesToLong(bytesLong2, ByteOrder.LITTLE_ENDIAN);
        Assert.equals(long1, long3);

        byte[] bytesLong3 = ByteUtil.longToBytes(long1, ByteOrder.LITTLE_ENDIAN);
        long long4 = ByteUtil.bytesToLong(bytesLong3);
        Assert.equals(long1, long4);
    }

    @Test
    public void longAndBytesBigEndianTest() {
        // 测试大端序 long 转 byte 数组
        long long1 = RandomUtil.randomLong(Long.MAX_VALUE);

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(long1);
        byte[] bytesLongFromBuffer = buffer.array();

        byte[] bytesLong = ByteUtil.longToBytes(long1, ByteOrder.BIG_ENDIAN);
        Assert.equals(bytesLongFromBuffer, bytesLong);

        long long2 = ByteUtil.bytesToLong(bytesLong, ByteOrder.BIG_ENDIAN);
        Assert.equals(long1, long2);
    }

    @Test
    public void floatAndBytesLittleEndianTest() {
        // 测试 long 转 byte 数组
        float f1 = (float) RandomUtil.randomDouble();

        byte[] bytesLong = ByteUtil.floatToBytes(f1, ByteOrder.LITTLE_ENDIAN);
        float f2 = ByteUtil.bytesToFloat(bytesLong, ByteOrder.LITTLE_ENDIAN);
        Assert.equals(f1, f2);
    }

    @Test
    public void floatAndBytesBigEndianTest() {
        // 测试大端序 long 转 byte 数组
        float f1 = (float) RandomUtil.randomDouble();

        byte[] bytesLong = ByteUtil.floatToBytes(f1, ByteOrder.BIG_ENDIAN);
        float f2 = ByteUtil.bytesToFloat(bytesLong, ByteOrder.BIG_ENDIAN);

        Assert.equals(f1, f2);
    }

    @Test
    public void shortAndBytesLittleEndianTest() {
        short short1 = (short) RandomUtil.randomInt();

        byte[] bytes = ByteUtil.shortToBytes(short1, ByteOrder.LITTLE_ENDIAN);
        short short2 = ByteUtil.bytesToShort(bytes, ByteOrder.LITTLE_ENDIAN);
        Assert.equals(short2, short1);

        byte[] bytes2 = ByteUtil.shortToBytes(short1);
        short short3 = ByteUtil.bytesToShort(bytes2, ByteOrder.LITTLE_ENDIAN);
        Assert.equals(short3, short1);

        byte[] bytes3 = ByteUtil.shortToBytes(short1, ByteOrder.LITTLE_ENDIAN);
        short short4 = ByteUtil.bytesToShort(bytes3);
        Assert.equals(short4, short1);
    }

    @Test
    public void shortAndBytesBigEndianTest() {
        short short1 = 122;
        byte[] bytes = ByteUtil.shortToBytes(short1, ByteOrder.BIG_ENDIAN);
        short short2 = ByteUtil.bytesToShort(bytes, ByteOrder.BIG_ENDIAN);

        Assert.equals(short2, short1);
    }

    @Test
    public void bytesToLongTest() {
        long a = RandomUtil.randomLong(0, Long.MAX_VALUE);
        ByteBuffer wrap = ByteBuffer.wrap(ByteUtil.longToBytes(a));
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        long aLong = wrap.getLong();
        Assert.equals(a, aLong);

        wrap = ByteBuffer.wrap(ByteUtil.longToBytes(a, ByteOrder.BIG_ENDIAN));
        wrap.order(ByteOrder.BIG_ENDIAN);
        aLong = wrap.getLong();
        Assert.equals(a, aLong);
    }

    @Test
    public void bytesToIntTest() {
        int a = RandomUtil.randomInt(0, Integer.MAX_VALUE);
        ByteBuffer wrap = ByteBuffer.wrap(ByteUtil.intToBytes(a));
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        int aInt = wrap.getInt();
        Assert.equals(a, aInt);

        wrap = ByteBuffer.wrap(ByteUtil.intToBytes(a, ByteOrder.BIG_ENDIAN));
        wrap.order(ByteOrder.BIG_ENDIAN);
        aInt = wrap.getInt();
        Assert.equals(a, aInt);
    }

    @Test
    public void bytesToShortTest() {
        short a = (short) RandomUtil.randomInt(0, Short.MAX_VALUE);

        ByteBuffer wrap = ByteBuffer.wrap(ByteUtil.shortToBytes(a));
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        short aShort = wrap.getShort();
        Assert.equals(a, aShort);

        wrap = ByteBuffer.wrap(ByteUtil.shortToBytes(a, ByteOrder.BIG_ENDIAN));
        wrap.order(ByteOrder.BIG_ENDIAN);
        aShort = wrap.getShort();
        Assert.equals(a, aShort);
    }
}
