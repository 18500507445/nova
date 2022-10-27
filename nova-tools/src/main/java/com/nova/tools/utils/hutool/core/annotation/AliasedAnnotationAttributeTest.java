package com.nova.tools.utils.hutool.core.annotation;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.annotation.AliasedAnnotationAttribute;
import cn.hutool.core.annotation.CacheableAnnotationAttribute;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class AliasedAnnotationAttributeTest {

    @Test
    public void baseInfoTest() {
        // 组合属性
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        //final AliasedAnnotationAttribute valueAnnotationAttribute = new AliasedAnnotationAttribute(valueAttribute, nameAttribute);

        //// 注解属性
        //Assert.equals(annotation, valueAnnotationAttribute.getAnnotation());
        //Assert.equals(annotation.annotationType(), valueAnnotationAttribute.getAnnotationType());
        //
        //// 方法属性
        //Assert.equals(valueMethod.getAnnotation(Alias.class), valueAnnotationAttribute.getAnnotation(Alias.class));
        //Assert.equals(valueMethod.getName(), valueAnnotationAttribute.getAttributeName());
        //Assert.equals(nameMethod.getReturnType(), valueAnnotationAttribute.getAttributeType());
    }

    @Test
    public void workWhenValueDefaultTest() {
        // 组合属性
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        //final AliasedAnnotationAttribute annotationAttribute = new AliasedAnnotationAttribute(valueAttribute, nameAttribute);
        //
        //// 值处理
        //Assert.equals("name", annotationAttribute.getValue());
        //Assert.isFalse(annotationAttribute.isValueEquivalentToDefaultValue());
        //Assert.isTrue(annotationAttribute.isWrapped());
    }

    @Test
    public void workWhenValueNonDefaultTest() {
        // 组合属性
        final Annotation annotation = ClassForTest2.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        //final AliasedAnnotationAttribute annotationAttribute = new AliasedAnnotationAttribute(valueAttribute, nameAttribute);
        //
        //// 值处理
        //Assert.equals("value", annotationAttribute.getValue());
        //Assert.isFalse(annotationAttribute.isValueEquivalentToDefaultValue());
        //Assert.isTrue(annotationAttribute.isWrapped());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @interface AnnotationForTest {
        @Alias("value")
        String value() default "";

        String name() default "";
    }

    @AnnotationForTest(name = "name", value = "value")
    static class ClassForTest1 {
    }

    @AnnotationForTest(value = "value")
    static class ClassForTest2 {
    }

}
