package com.nova.common.utils.string;

import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @description: 余弦相似度用向量空间中两个向量夹角的余弦值作为衡量两个个体间差异的大小。
 * 余弦值越接近1，就表明夹角越接近0度，也就是两个向量越相似，这就叫"余弦相似性"
 * @author: wzh
 * @see <a href="https://blog.csdn.net/fycghy0803/article/details/79880452">余弦相似度算法</a>
 * @date: 2020/4/20 19:27
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MatchUtil {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        //要比较的两个字符串
        String str1 = "今天是星期五";
        String str2 = "今天是星期";
        Integer num = matchValue(str1, str2);
        System.err.println("相似度:" + num + "% ，耗时：" + (System.currentTimeMillis() - start) + "ms");

        long start1 = System.currentTimeMillis();
        double similar = StrUtil.similar(str1, str2);
        System.err.println("similar相似度:" + similar + "% ，耗时：" + (System.currentTimeMillis() - start1) + "ms");
    }

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
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                        dif[i - 1][j] + 1);
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


}
