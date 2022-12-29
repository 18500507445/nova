package com.nova.tools.utils.hutool.core.annotation;

import cn.hutool.core.annotation.CacheableAnnotationAttribute;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class CacheableAnnotationAttributeTest {

    @Test
    public void baseInfoTest() {
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method attribute = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute annotationAttribute = new CacheableAnnotationAttribute(annotation, attribute);
        // 注解属性
        Assert.equals(annotation, annotationAttribute.getAnnotation());
        Assert.equals(annotation.annotationType(), annotationAttribute.getAnnotationType());
        // 方法属性
        Assert.equals(attribute.getName(), annotationAttribute.getAttributeName());
        Assert.equals(attribute.getReturnType(), annotationAttribute.getAttributeType());
    }

    @Test
    public void workWhenValueDefaultTest() {
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method attribute = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute annotationAttribute = new CacheableAnnotationAttribute(annotation, attribute);

        // 值处理
        Assert.equals("", annotationAttribute.getValue());
        Assert.isTrue(annotationAttribute.isValueEquivalentToDefaultValue());
        Assert.isFalse(annotationAttribute.isWrapped());
    }

    @Test
    public void workWhenValueNonDefaultTest() {
        final Annotation annotation = ClassForTest2.class.getAnnotation(AnnotationForTest.class);
        final Method attribute = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute annotationAttribute = new CacheableAnnotationAttribute(annotation, attribute);

        // 值处理
        Assert.equals("test", annotationAttribute.getValue());
        Assert.isFalse(annotationAttribute.isValueEquivalentToDefaultValue());
        Assert.isFalse(annotationAttribute.isWrapped());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @interface AnnotationForTest {
        String value() default "";
    }

    @AnnotationForTest("")
    static class ClassForTest1 {
    }

    @AnnotationForTest("test")
    static class ClassForTest2 {
    }

}
