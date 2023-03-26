package com.nova.tools.utils.guava;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/13 18:25
 */
class ObjectUtilsTest {

    /**
     * System.identityHashCode(obj):不管obj所在的类有没有重写hashcode方法，identityHashCode
     * 始终调用的都是Object.hashCode()
     */
    @Test
    public void test2() {
        // java.lang.String@7d6f77cc
        System.out.println(ObjectUtils.identityToString("abc"));
    }

    /**
     * str1,str2,str3....取第一个不为空的作为结果使用
     */
    @Test
    public void test1() {
        String str1 = null;
        String str2 = null;
        String str3 = "str233";
        System.out.println(ObjectUtils.firstNonNull(str1, str2, str3));
    }
}
