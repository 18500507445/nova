package com.nova.tools.utils.hutool.core.annotation.scanner;

import cn.hutool.core.annotation.scanner.ElementAnnotationScanner;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementAnnotationScannerTest {

    @Test
    public void supportTest() {
        final ElementAnnotationScanner scanner = new ElementAnnotationScanner();
        Assert.isTrue(scanner.support(ReflectUtil.getField(FieldAnnotationScannerTest.Example.class, "id")));
        Assert.isTrue(scanner.support(ReflectUtil.getMethod(FieldAnnotationScannerTest.Example.class, "getId")));
        Assert.isFalse(scanner.support(null));
        Assert.isTrue(scanner.support(FieldAnnotationScannerTest.Example.class));
    }

    @Test
    public void getAnnotationsTest() {
        final ElementAnnotationScanner scanner = new ElementAnnotationScanner();
        final Field field = ReflectUtil.getField(FieldAnnotationScannerTest.Example.class, "id");
        Assert.notNull(field);
        Assert.isTrue(scanner.support(field));
        List<Annotation> annotations = scanner.getAnnotations(field);
        Assert.equals(1, annotations.size());
        Assert.equals(AnnotationForScannerTest.class, CollUtil.getFirst(annotations).annotationType());
    }

    @Test
    public void scanTest() {
        final ElementAnnotationScanner scanner = new ElementAnnotationScanner();
        final Field field = ReflectUtil.getField(FieldAnnotationScannerTest.Example.class, "id");
        final Map<Integer, List<Annotation>> map = new HashMap<>();
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
