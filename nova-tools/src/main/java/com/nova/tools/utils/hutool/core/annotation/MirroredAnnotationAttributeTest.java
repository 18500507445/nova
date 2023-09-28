package com.nova.tools.utils.hutool.core.annotation;

import cn.hutool.core.annotation.CacheableAnnotationAttribute;
import cn.hutool.core.annotation.MirroredAnnotationAttribute;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class MirroredAnnotationAttributeTest {

    @Test
    public void baseInfoTest() {
        // 组合属性
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final MirroredAnnotationAttribute nameAnnotationAttribute = new MirroredAnnotationAttribute(nameAttribute, valueAttribute);

        // 注解属性
        Assert.equals(annotation, nameAnnotationAttribute.getAnnotation());
        Assert.equals(annotation.annotationType(), nameAnnotationAttribute.getAnnotationType());

        // 方法属性
        Assert.equals(nameMethod.getName(), nameAnnotationAttribute.getAttributeName());
        Assert.equals(nameMethod.getReturnType(), nameAnnotationAttribute.getAttributeType());
    }

    @Test
    public void workWhenValueDefaultTest() {
        // 组合属性
        final Annotation annotation = ClassForTest2.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final MirroredAnnotationAttribute nameAnnotationAttribute = new MirroredAnnotationAttribute(nameAttribute, valueAttribute);

        // 值处理
        Assert.equals("", nameAnnotationAttribute.getValue());
        Assert.isTrue(nameAnnotationAttribute.isValueEquivalentToDefaultValue());
        Assert.isTrue(nameAnnotationAttribute.isWrapped());
    }

    @Test
    public void workWhenValueNonDefaultTest() {
        // 组合属性
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final MirroredAnnotationAttribute nameAnnotationAttribute = new MirroredAnnotationAttribute(nameAttribute, valueAttribute);

        // 值处理
        Assert.equals("name", nameAnnotationAttribute.getValue());
        Assert.isFalse(nameAnnotationAttribute.isValueEquivalentToDefaultValue());
        Assert.isTrue(nameAnnotationAttribute.isWrapped());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @interface AnnotationForTest {
        String value() default "";

        String name() default "";
    }

    @AnnotationForTest(value = "name")
    static class ClassForTest1 {
    }

    @AnnotationForTest
    static class ClassForTest2 {
    }

}
