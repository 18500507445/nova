package com.nova.tools.utils.hutool.core.annotation.scanner;

import cn.hutool.core.annotation.scanner.AnnotationScanner;
import cn.hutool.core.annotation.scanner.FieldAnnotationScanner;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldAnnotationScannerTest {

    @Test
    public void supportTest() {
        AnnotationScanner scanner = new FieldAnnotationScanner();
        Assert.isTrue(scanner.support(ReflectUtil.getField(Example.class, "id")));
        Assert.isFalse(scanner.support(ReflectUtil.getMethod(Example.class, "getId")));
        Assert.isFalse(scanner.support(null));
        Assert.isFalse(scanner.support(Example.class));
    }

    @Test
    public void getAnnotationsTest() {
        AnnotationScanner scanner = new FieldAnnotationScanner();
        Field field = ReflectUtil.getField(Example.class, "id");
        Assert.notNull(field);
        Assert.isTrue(scanner.support(field));
        List<Annotation> annotations = scanner.getAnnotations(field);
        Assert.equals(1, annotations.size());
        Assert.equals(AnnotationForScannerTest.class, CollUtil.getFirst(annotations).annotationType());
    }

    @Test
    public void scanTest() {
        AnnotationScanner scanner = new FieldAnnotationScanner();
        Field field = ReflectUtil.getField(Example.class, "id");
        Map<Integer, List<Annotation>> map = new HashMap<>();
        scanner.scan(
                (index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
                field, null
        );
        Assert.equals(1, map.size());
        Assert.equals(1, map.get(0).size());
        Assert.equals(AnnotationForScannerTest.class, map.get(0).get(0).annotationType());
    }

    public static class Example {
        @AnnotationForScannerTest
        private Integer id;

        public Integer getId() {
            return id;
        }
    }

}
