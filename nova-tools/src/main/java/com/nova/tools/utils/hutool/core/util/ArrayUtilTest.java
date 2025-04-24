package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * {@link ArrayUtil} 数组工具单元测试
 *
 * @author:Looly
 */
public class ArrayUtilTest {

    @Test
    public void isEmptyTest() {
        int[] a = {};
        Assert.isTrue(ArrayUtil.isEmpty(a));
        Assert.isTrue(ArrayUtil.isEmpty((Object) a));
        int[] b = null;
        //noinspection ConstantConditions
        Assert.isTrue(ArrayUtil.isEmpty(b));
        Object c = null;
        //noinspection ConstantConditions
        Assert.isTrue(ArrayUtil.isEmpty(c));

        Object d = new Object[]{"1", "2", 3, 4D};
        boolean isEmpty = ArrayUtil.isEmpty(d);
        Assert.isFalse(isEmpty);
        d = new Object[0];
        isEmpty = ArrayUtil.isEmpty(d);
        Assert.isTrue(isEmpty);
        d = null;
        //noinspection ConstantConditions
        isEmpty = ArrayUtil.isEmpty(d);
        //noinspection ConstantConditions
        Assert.isTrue(isEmpty);

        // Object数组
        Object[] e = new Object[]{"1", "2", 3, 4D};
        final boolean empty = ArrayUtil.isEmpty(e);
        Assert.isFalse(empty);
    }

    @Test
    public void isNotEmptyTest() {
        int[] a = {1, 2};
        Assert.isTrue(ArrayUtil.isNotEmpty(a));

        String[] b = {"a", "b", "c"};
        Assert.isTrue(ArrayUtil.isNotEmpty(b));

        Object c = new Object[]{"1", "2", 3, 4D};
        Assert.isTrue(ArrayUtil.isNotEmpty(c));
    }

    @Test
    public void newArrayTest() {
        String[] newArray = ArrayUtil.newArray(String.class, 3);
        Assert.equals(3, newArray.length);
    }

    @Test
    public void cloneTest() {
        Integer[] b = {1, 2, 3};
        Integer[] cloneB = ArrayUtil.clone(b);
        Assert.equals(b, cloneB);

        int[] a = {1, 2, 3};
        int[] clone = ArrayUtil.clone(a);
        Assert.equals(a, clone);
    }

    @Test
    public void filterEditTest() {
        Integer[] a = {1, 2, 3, 4, 5, 6};
        Integer[] filter = ArrayUtil.edit(a, t -> (t % 2 == 0) ? t : null);
        Assert.equals(filter, new Integer[]{2, 4, 6});
    }

    @Test
    public void filterTestForFilter() {
        Integer[] a = {1, 2, 3, 4, 5, 6};
        Integer[] filter = ArrayUtil.filter(a, t -> t % 2 == 0);
        Assert.equals(filter, new Integer[]{2, 4, 6});
    }

    @Test
    public void editTest() {
        Integer[] a = {1, 2, 3, 4, 5, 6};
        Integer[] filter = ArrayUtil.edit(a, t -> (t % 2 == 0) ? t * 10 : t);
        Assert.equals(filter, new Integer[]{1, 20, 3, 40, 5, 60});
    }

    @Test
    public void indexOfTest() {
        Integer[] a = {1, 2, 3, 4, 5, 6};
        int index = ArrayUtil.indexOf(a, 3);
        Assert.equals(2, index);

        long[] b = {1, 2, 3, 4, 5, 6};
        int index2 = ArrayUtil.indexOf(b, 3);
        Assert.equals(2, index2);
    }

    @Test
    public void lastIndexOfTest() {
        Integer[] a = {1, 2, 3, 4, 3, 6};
        int index = ArrayUtil.lastIndexOf(a, 3);
        Assert.equals(4, index);

        long[] b = {1, 2, 3, 4, 3, 6};
        int index2 = ArrayUtil.lastIndexOf(b, 3);
        Assert.equals(4, index2);
    }

    @Test
    public void containsTest() {
        Integer[] a = {1, 2, 3, 4, 3, 6};
        boolean contains = ArrayUtil.contains(a, 3);
        Assert.isTrue(contains);

        long[] b = {1, 2, 3, 4, 3, 6};
        boolean contains2 = ArrayUtil.contains(b, 3);
        Assert.isTrue(contains2);
    }

    @Test
    public void containsAnyTest() {
        Integer[] a = {1, 2, 3, 4, 3, 6};
        boolean contains = ArrayUtil.containsAny(a, 4, 10, 40);
        Assert.isTrue(contains);

        contains = ArrayUtil.containsAny(a, 10, 40);
        Assert.isFalse(contains);
    }

    @Test
    public void containsAllTest() {
        Integer[] a = {1, 2, 3, 4, 3, 6};
        boolean contains = ArrayUtil.containsAll(a, 4, 2, 6);
        Assert.isTrue(contains);

        contains = ArrayUtil.containsAll(a, 1, 2, 3, 5);
        Assert.isFalse(contains);
    }

    @Test
    public void mapTest() {
        String[] keys = {"a", "b", "c"};
        Integer[] values = {1, 2, 3};
        Map<String, Integer> map = ArrayUtil.zip(keys, values, true);
        Assert.equals(Objects.requireNonNull(map).toString(), "{a=1, b=2, c=3}");
    }

    @Test
    public void castTest() {
        Object[] values = {"1", "2", "3"};
        String[] cast = (String[]) ArrayUtil.cast(String.class, values);
        Assert.equals(values[0], cast[0]);
        Assert.equals(values[1], cast[1]);
        Assert.equals(values[2], cast[2]);
    }

    @Test
    public void rangeTest() {
        int[] range = ArrayUtil.range(0, 10);
        Assert.equals(0, range[0]);
        Assert.equals(1, range[1]);
        Assert.equals(2, range[2]);
        Assert.equals(3, range[3]);
        Assert.equals(4, range[4]);
        Assert.equals(5, range[5]);
        Assert.equals(6, range[6]);
        Assert.equals(7, range[7]);
        Assert.equals(8, range[8]);
        Assert.equals(9, range[9]);
    }

    @Test
    public void rangeMinTest() {
        //noinspection ResultOfMethodCallIgnored
        ArrayUtil.range(0, Integer.MIN_VALUE);
    }

    @Test
    public void maxTest() {
        int max = ArrayUtil.max(1, 2, 13, 4, 5);
        Assert.equals(13, max);

        long maxLong = ArrayUtil.max(1L, 2L, 13L, 4L, 5L);
        Assert.equals(13, maxLong);

        double maxDouble = ArrayUtil.max(1D, 2.4D, 13.0D, 4.55D, 5D);
        Assert.equals(13.0, maxDouble);

        BigDecimal one = new BigDecimal("1.00");
        BigDecimal two = new BigDecimal("2.0");
        BigDecimal three = new BigDecimal("3");
        BigDecimal[] bigDecimals = {two, one, three};

        BigDecimal minAccuracy = ArrayUtil.min(bigDecimals, Comparator.comparingInt(BigDecimal::scale));
        Assert.equals(minAccuracy, three);

        BigDecimal maxAccuracy = ArrayUtil.max(bigDecimals, Comparator.comparingInt(BigDecimal::scale));
        Assert.equals(maxAccuracy, one);
    }

    @Test
    public void minTest() {
        int min = ArrayUtil.min(1, 2, 13, 4, 5);
        Assert.equals(1, min);

        long minLong = ArrayUtil.min(1L, 2L, 13L, 4L, 5L);
        Assert.equals(1, minLong);

        double minDouble = ArrayUtil.min(1D, 2.4D, 13.0D, 4.55D, 5D);
        Assert.equals(1.0, minDouble);
    }

    @Test
    public void appendTest() {
        String[] a = {"1", "2", "3", "4"};
        String[] b = {"a", "b", "c"};

        String[] result = ArrayUtil.append(a, b);
        Assert.equals(new String[]{"1", "2", "3", "4", "a", "b", "c"}, result);
    }

    @Test
    public void insertTest() {
        String[] a = {"1", "2", "3", "4"};
        String[] b = {"a", "b", "c"};

        // 在-1位置插入，相当于在3位置插入
        String[] result = ArrayUtil.insert(a, -1, b);
        Assert.equals(new String[]{"1", "2", "3", "a", "b", "c", "4"}, result);

        // 在第0个位置插入，即在数组前追加
        result = ArrayUtil.insert(a, 0, b);
        Assert.equals(new String[]{"a", "b", "c", "1", "2", "3", "4"}, result);

        // 在第2个位置插入，即"3"之前
        result = ArrayUtil.insert(a, 2, b);
        Assert.equals(new String[]{"1", "2", "a", "b", "c", "3", "4"}, result);

        // 在第4个位置插入，即"4"之后，相当于追加
        result = ArrayUtil.insert(a, 4, b);
        Assert.equals(new String[]{"1", "2", "3", "4", "a", "b", "c"}, result);

        // 在第5个位置插入，由于数组长度为4，因此补null
        result = ArrayUtil.insert(a, 5, b);
        Assert.equals(new String[]{"1", "2", "3", "4", null, "a", "b", "c"}, result);
    }

    @Test
    public void joinTest() {
        String[] array = {"aa", "bb", "cc", "dd"};
        String join = ArrayUtil.join(array, ",", "[", "]");
        Assert.equals("[aa],[bb],[cc],[dd]", join);

        Object array2 = new String[]{"aa", "bb", "cc", "dd"};
        String join2 = ArrayUtil.join(array2, ",");
        Assert.equals("aa,bb,cc,dd", join2);
    }

    @Test
    public void getArrayTypeTest() {
        Class<?> arrayType = ArrayUtil.getArrayType(int.class);
        Assert.equals(int[].class, arrayType);

        arrayType = ArrayUtil.getArrayType(String.class);
        Assert.equals(String[].class, arrayType);
    }

    @Test
    public void distinctTest() {
        String[] array = {"aa", "bb", "cc", "dd", "bb", "dd"};
        String[] distinct = ArrayUtil.distinct(array);
        Assert.equals(new String[]{"aa", "bb", "cc", "dd"}, distinct);
    }

    @Test
    public void distinctByFunctionTest() {
        String[] array = {"aa", "Aa", "BB", "bb"};

        // 覆盖模式下，保留最后加入的两个元素
        String[] distinct = ArrayUtil.distinct(array, String::toLowerCase, true);
        Assert.equals(new String[]{"Aa", "bb"}, distinct);

        // 忽略模式下，保留最早加入的两个元素
        distinct = ArrayUtil.distinct(array, String::toLowerCase, false);
        Assert.equals(new String[]{"aa", "BB"}, distinct);
    }

    @Test
    public void toStingTest() {
        int[] a = {1, 3, 56, 6, 7};
        Assert.equals("[1, 3, 56, 6, 7]", ArrayUtil.toString(a));
        long[] b = {1, 3, 56, 6, 7};
        Assert.equals("[1, 3, 56, 6, 7]", ArrayUtil.toString(b));
        short[] c = {1, 3, 56, 6, 7};
        Assert.equals("[1, 3, 56, 6, 7]", ArrayUtil.toString(c));
        double[] d = {1, 3, 56, 6, 7};
        Assert.equals("[1.0, 3.0, 56.0, 6.0, 7.0]", ArrayUtil.toString(d));
        byte[] e = {1, 3, 56, 6, 7};
        Assert.equals("[1, 3, 56, 6, 7]", ArrayUtil.toString(e));
        boolean[] f = {true, false, true, true, true};
        Assert.equals("[true, false, true, true, true]", ArrayUtil.toString(f));
        float[] g = {1, 3, 56, 6, 7};
        Assert.equals("[1.0, 3.0, 56.0, 6.0, 7.0]", ArrayUtil.toString(g));
        char[] h = {'a', 'b', '你', '好', '1'};
        Assert.equals("[a, b, 你, 好, 1]", ArrayUtil.toString(h));

        String[] array = {"aa", "bb", "cc", "dd", "bb", "dd"};
        Assert.equals("[aa, bb, cc, dd, bb, dd]", ArrayUtil.toString(array));
    }

    @Test
    public void toArrayTest() {
        final ArrayList<String> list = CollUtil.newArrayList("A", "B", "C", "D");
        final String[] array = ArrayUtil.toArray(list, String.class);
        Assert.equals("A", array[0]);
        Assert.equals("B", array[1]);
        Assert.equals("C", array[2]);
        Assert.equals("D", array[3]);
    }

    @Test
    public void addAllTest() {
        final int[] ints = ArrayUtil.addAll(new int[]{1, 2, 3}, new int[]{4, 5, 6});
        Assert.equals(new int[]{1, 2, 3, 4, 5, 6}, ints);
    }

    @Test
    public void isAllNotNullTest() {
        String[] allNotNull = {"aa", "bb", "cc", "dd", "bb", "dd"};
        Assert.isTrue(ArrayUtil.isAllNotNull(allNotNull));
        String[] hasNull = {"aa", "bb", "cc", null, "bb", "dd"};
        Assert.isFalse(ArrayUtil.isAllNotNull(hasNull));
    }

    @Test
    public void indexOfSubTest() {
        Integer[] a = {0x12, 0x34, 0x56, 0x78, 0x9A};
        Integer[] b = {0x56, 0x78};
        Integer[] c = {0x12, 0x56};
        Integer[] d = {0x78, 0x9A};
        Integer[] e = {0x78, 0x9A, 0x10};

        int i = ArrayUtil.indexOfSub(a, b);
        Assert.equals(2, i);

        i = ArrayUtil.indexOfSub(a, c);
        Assert.equals(-1, i);

        i = ArrayUtil.indexOfSub(a, d);
        Assert.equals(3, i);

        i = ArrayUtil.indexOfSub(a, e);
        Assert.equals(-1, i);

        i = ArrayUtil.indexOfSub(a, null);
        Assert.equals(-1, i);

        i = ArrayUtil.indexOfSub(null, null);
        Assert.equals(-1, i);

        i = ArrayUtil.indexOfSub(null, b);
        Assert.equals(-1, i);
    }

    @Test
    public void indexOfSubTest2() {
        Integer[] a = {0x12, 0x56, 0x34, 0x56, 0x78, 0x9A};
        Integer[] b = {0x56, 0x78};
        int i = ArrayUtil.indexOfSub(a, b);
        Assert.equals(3, i);
    }

    @Test
    public void lastIndexOfSubTest() {
        Integer[] a = {0x12, 0x34, 0x56, 0x78, 0x9A};
        Integer[] b = {0x56, 0x78};
        Integer[] c = {0x12, 0x56};
        Integer[] d = {0x78, 0x9A};
        Integer[] e = {0x78, 0x9A, 0x10};

        int i = ArrayUtil.lastIndexOfSub(a, b);
        Assert.equals(2, i);

        i = ArrayUtil.lastIndexOfSub(a, c);
        Assert.equals(-1, i);

        i = ArrayUtil.lastIndexOfSub(a, d);
        Assert.equals(3, i);

        i = ArrayUtil.lastIndexOfSub(a, e);
        Assert.equals(-1, i);

        i = ArrayUtil.lastIndexOfSub(a, null);
        Assert.equals(-1, i);

        i = ArrayUtil.lastIndexOfSub(null, null);
        Assert.equals(-1, i);

        i = ArrayUtil.lastIndexOfSub(null, b);
        Assert.equals(-1, i);
    }

    @Test
    public void lastIndexOfSubTest2() {
        Integer[] a = {0x12, 0x56, 0x78, 0x56, 0x21, 0x9A};
        Integer[] b = {0x56, 0x78};
        int i = ArrayUtil.indexOfSub(a, b);
        Assert.equals(1, i);
    }

    @Test
    public void reverseTest() {
        int[] a = {1, 2, 3, 4};
        final int[] reverse = ArrayUtil.reverse(a);
        Assert.equals(new int[]{4, 3, 2, 1}, reverse);
    }

    @Test
    public void reverseTest2s() {
        Object[] a = {"1", '2', "3", 4};
        final Object[] reverse = ArrayUtil.reverse(a);
        Assert.equals(new Object[]{4, "3", '2', "1"}, reverse);
    }

    @Test
    public void removeEmptyTest() {
        String[] a = {"a", "b", "", null, " ", "c"};
        String[] resultA = {"a", "b", " ", "c"};
        Assert.equals(ArrayUtil.removeEmpty(a), resultA);
    }

    @Test
    public void removeBlankTest() {
        String[] a = {"a", "b", "", null, " ", "c"};
        String[] resultA = {"a", "b", "c"};
        Assert.equals(ArrayUtil.removeBlank(a), resultA);
    }

    @Test
    public void nullToEmptyTest() {
        String[] a = {"a", "b", "", null, " ", "c"};
        String[] resultA = {"a", "b", "", "", " ", "c"};
        Assert.equals(ArrayUtil.nullToEmpty(a), resultA);
    }

    @Test
    public void wrapTest() {
        Object a = new int[]{1, 2, 3, 4};
        Object[] wrapA = ArrayUtil.wrap(a);
        for (Object o : wrapA) {
            Assert.isTrue(o instanceof Integer);
        }
    }

    @Test
    public void splitTest() {
        byte[] array = new byte[1024];
        byte[][] arrayAfterSplit = ArrayUtil.split(array, 500);
        Assert.equals(3, arrayAfterSplit.length);
        Assert.equals(24, arrayAfterSplit[2].length);
    }

    @Test
    public void getTest() {
        String[] a = {"a", "b", "c"};
        final Object o = ArrayUtil.get(a, -1);
        Assert.equals("c", o);
    }

    @Test
    public void replaceTest() {
        String[] a = {"1", "2", "3", "4"};
        String[] b = {"a", "b", "c"};

        // 在小于0的位置，-1位置插入，返回b+a，新数组
        String[] result = ArrayUtil.replace(a, -1, b);
        Assert.equals(new String[]{"a", "b", "c", "1", "2", "3", "4"}, result);

        // 在第0个位置开始替换，返回a
        result = ArrayUtil.replace(ArrayUtil.clone(a), 0, b);
        Assert.equals(new String[]{"a", "b", "c", "4"}, result);

        // 在第1个位置替换，即"2"开始
        result = ArrayUtil.replace(ArrayUtil.clone(a), 1, b);
        Assert.equals(new String[]{"1", "a", "b", "c"}, result);

        // 在第2个位置插入，即"3"之后
        result = ArrayUtil.replace(ArrayUtil.clone(a), 2, b);
        Assert.equals(new String[]{"1", "2", "a", "b", "c"}, result);

        // 在第3个位置插入，即"4"之后
        result = ArrayUtil.replace(ArrayUtil.clone(a), 3, b);
        Assert.equals(new String[]{"1", "2", "3", "a", "b", "c"}, result);

        // 在第4个位置插入，数组长度为4，在索引4出替换即两个数组相加
        result = ArrayUtil.replace(ArrayUtil.clone(a), 4, b);
        Assert.equals(new String[]{"1", "2", "3", "4", "a", "b", "c"}, result);

        // 在大于3个位置插入，数组长度为4，即两个数组相加
        result = ArrayUtil.replace(ArrayUtil.clone(a), 5, b);
        Assert.equals(new String[]{"1", "2", "3", "4", "a", "b", "c"}, result);

        String[] e = null;
        String[] f = {"a", "b", "c"};

        // e为null 返回 f
        result = ArrayUtil.replace(e, -1, f);
        Assert.equals(f, result);

        String[] g = {"a", "b", "c"};
        String[] h = null;

        // h为null 返回 g
        result = ArrayUtil.replace(g, 0, h);
        Assert.equals(g, result);
    }

    @Test
    public void setOrAppendTest() {
        String[] arr = new String[0];
        String[] newArr = ArrayUtil.setOrAppend(arr, 0, "Good");// ClassCastException
        Assert.equals(new String[]{"Good"}, newArr);
    }

    @Test
    public void getAnyTest() {
        final String[] a = {"a", "b", "c", "d", "e"};
        final Object o = ArrayUtil.getAny(a, 3, 4);
        final String[] resultO = (String[]) o;
        final String[] c = {"d", "e"};
        Assert.isTrue(ArrayUtil.containsAll(c, resultO[0], resultO[1]));
    }
}
