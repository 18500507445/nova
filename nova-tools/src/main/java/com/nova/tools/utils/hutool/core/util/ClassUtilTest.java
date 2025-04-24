package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * {@link ClassUtil} 单元测试
 *
 * @author:Looly
 */
public class ClassUtilTest {

    @Test
    public void getClassNameTest() {
        String className = ClassUtil.getClassName(ClassUtil.class, false);
        Assert.equals("cn.hutool.core.util.ClassUtil", className);

        String simpleClassName = ClassUtil.getClassName(ClassUtil.class, true);
        Assert.equals("ClassUtil", simpleClassName);
    }

    @SuppressWarnings("unused")
    static class TestClass {
        private String privateField;
        protected String field;

        private void privateMethod() {
        }

        public void publicMethod() {
        }
    }

    @SuppressWarnings({"unused", "InnerClassMayBeStatic"})
    class TestSubClass extends TestClass {
        private String subField;

        private void privateSubMethod() {
        }

        public void publicSubMethod() {
        }

    }

    @Test
    public void getPublicMethod() {
        Method superPublicMethod = ClassUtil.getPublicMethod(TestSubClass.class, "publicMethod");
        Assert.notNull(superPublicMethod);
        Method superPrivateMethod = ClassUtil.getPublicMethod(TestSubClass.class, "privateMethod");
        Assert.isNull(superPrivateMethod);

        Method publicMethod = ClassUtil.getPublicMethod(TestSubClass.class, "publicSubMethod");
        Assert.notNull(publicMethod);
        Method privateMethod = ClassUtil.getPublicMethod(TestSubClass.class, "privateSubMethod");
        Assert.isNull(privateMethod);
    }

    @Test
    public void getDeclaredMethod() {
        Method noMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "noMethod");
        Assert.isNull(noMethod);

        Method privateMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "privateMethod");
        Assert.notNull(privateMethod);
        Method publicMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "publicMethod");
        Assert.notNull(publicMethod);

        Method publicSubMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "publicSubMethod");
        Assert.notNull(publicSubMethod);
        Method privateSubMethod = ClassUtil.getDeclaredMethod(TestSubClass.class, "privateSubMethod");
        Assert.notNull(privateSubMethod);

    }

    @Test
    public void getDeclaredField() {
        Field noField = ClassUtil.getDeclaredField(TestSubClass.class, "noField");
        Assert.isNull(noField);

        // 获取不到父类字段
        Field field = ClassUtil.getDeclaredField(TestSubClass.class, "field");
        Assert.isNull(field);

        Field subField = ClassUtil.getDeclaredField(TestSubClass.class, "subField");
        Assert.notNull(subField);
    }

    @Test
    public void getClassPathTest() {
        String classPath = ClassUtil.getClassPath();
        Assert.notNull(classPath);
    }

    @Test
    public void getShortClassNameTest() {
        String className = "cn.hutool.core.util.StrUtil";
        String result = ClassUtil.getShortClassName(className);
        Assert.equals("c.h.c.u.StrUtil", result);
    }

    @Test
    public void getLocationPathTest() {
        final String classDir = ClassUtil.getLocationPath(ClassUtilTest.class);
        Assert.isTrue(Objects.requireNonNull(classDir).endsWith("/hutool-core/target/test-classes/"));
    }
}
