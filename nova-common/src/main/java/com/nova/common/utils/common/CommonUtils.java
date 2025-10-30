package com.nova.common.utils.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: wzh
 * @description: 通用工具类
 * @date: 2024/01/08 16:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

    @Test
    public void testStrMatch() {
        TimeInterval timer = DateUtil.timer();
        //要比较的两个字符串
        String str1 = "今天是星期五";
        String str2 = "今天是星期";
        Integer num = matchValue(str1, str2);
        System.err.println("相似度:" + num + "% ，耗时：" + timer.interval() + "ms");

        timer.restart();
        double similar = StrUtil.similar(str1, str2);
        System.err.println("StrUtil相似度:" + similar + "% ，耗时：" + timer.interval() + "ms");
    }

    /**
     * @description: 余弦相似度用向量空间中两个向量夹角的余弦值作为衡量两个个体间差异的大小。
     * 余弦值越接近1，就表明夹角越接近0度，也就是两个向量越相似，这就叫"余弦相似性"
     * @see <a href="https://blog.csdn.net/fycghy0803/article/details/79880452">余弦相似度算法</a>
     */
    public static Integer matchValue(String str1, String str2) {
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        //赋初值，步骤B。
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1, dif[i - 1][j] + 1);
            }
        }
//        System.err.println("字符串\"" + str1 + "\"与\"" + str2 + "\"的比较");
        //取数组右下角的值，同样不同位置代表不同字符串的比较
//        System.err.println("差异步骤：" + dif[len1][len2]);
        //计算相似度
        double similarity = 1 - (double) dif[len1][len2] / Math.max(str1.length(), str2.length());
//        System.err.println("相似度：" + format);
        double v = similarity * 100;
        return (int) v;
    }

    //得到最小值
    private static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }


    /**
     * 参数非空校验
     */
    public static void checkNotNull(Object params) {
        if (null == params) {
            return;
        }
        Class<?> clazz = params.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (ArrayUtil.isEmpty(fields)) {
            return;
        }
        for (Field field : fields) {
            field.setAccessible(true);
            Object result = null;
            try {
                result = field.get(params);
            } catch (Exception ignore) {
            }
            if (Objects.isNull(result)) {
                String msg = String.format("字段 %s 为空", field.getName());
                throw new RuntimeException(msg);
            }
        }
    }


    /**
     * @description: 对比两个实体类，找到不同值的字段
     * 场景1：obj1和ob2都不为空，Map<field，Object[obj1.field.value，obj2.field.value]>
     * 场景2：obj1 或 ob2 有一个为空，Map<field，Object[obj1或obj2.field.value]>
     */
    public static Map<String, Object[]> compareObjects(Object obj1, Object obj2) throws IllegalAccessException {
        Map<String, Object[]> differences = new HashMap<>();
        Class<?> clazz;
        if (ObjectUtil.isNull(obj1)) {
            clazz = obj2.getClass();
        } else {
            clazz = obj1.getClass();
        }
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (ObjectUtil.isNull(obj1) && ObjectUtil.isNotNull(obj2)) {
                Object value2 = field.get(obj2);
                differences.put(field.getName(), new Object[]{value2});
            } else if (ObjectUtil.isNotNull(obj1) && ObjectUtil.isNull(obj2)) {
                Object value1 = field.get(obj1);
                differences.put(field.getName(), new Object[]{value1});
            } else {
                Object value1 = field.get(obj1);
                Object value2 = field.get(obj2);
                if (!Objects.equals(value1, value2)) {
                    differences.put(field.getName(), new Object[]{value1, value2});
                }
            }
        }
        return differences;
    }
}
