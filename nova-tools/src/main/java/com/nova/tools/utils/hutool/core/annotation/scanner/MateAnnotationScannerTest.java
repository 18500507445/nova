package com.nova.tools.utils.hutool.core.annotation.scanner;

import cn.hutool.core.annotation.scanner.AnnotationScanner;
import cn.hutool.core.annotation.scanner.MetaAnnotationScanner;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MateAnnotationScannerTest {

    @Test
    public void supportTest() {
        AnnotationScanner scanner = new MetaAnnotationScanner();
        Assert.isTrue(scanner.support(AnnotationForScannerTest.class));
        Assert.isFalse(scanner.support(ReflectUtil.getField(Example.class, "id")));
        Assert.isFalse(scanner.support(ReflectUtil.getMethod(Example.class, "getId")));
        Assert.isFalse(scanner.support(null));
        Assert.isFalse(scanner.support(Example.class));
    }

    @Test
    public void getAnnotationsTest() {
        AnnotationScanner scanner = new MetaAnnotationScanner();
        Assert.isTrue(scanner.support(AnnotationForScannerTest3.class));
        Map<Class<? extends Annotation>, Annotation> annotations = CollUtil.toMap(scanner.getAnnotations(AnnotationForScannerTest3.class), new HashMap<>(), Annotation::annotationType);
        Assert.equals(3, annotations.size());
        Assert.isTrue(annotations.containsKey(AnnotationForScannerTest.class));
        Assert.isTrue(annotations.containsKey(AnnotationForScannerTest1.class));
        Assert.isTrue(annotations.containsKey(AnnotationForScannerTest2.class));
        Assert.isFalse(annotations.containsKey(AnnotationForScannerTest3.class));

        scanner = new MetaAnnotationScanner(false);
        Assert.isTrue(scanner.support(AnnotationForScannerTest3.class));
        annotations = CollUtil.toMap(scanner.getAnnotations(AnnotationForScannerTest3.class), new HashMap<>(), Annotation::annotationType);
        Assert.equals(1, annotations.size());
        Assert.isTrue(annotations.containsKey(AnnotationForScannerTest2.class));
        Assert.isFalse(annotations.containsKey(AnnotationForScannerTest.class));
        Assert.isFalse(annotations.containsKey(AnnotationForScannerTest1.class));
        Assert.isFalse(annotations.containsKey(AnnotationForScannerTest3.class));
    }

    @Test
    public void scanTest() {
        AnnotationScanner scanner = new MetaAnnotationScanner();
        Map<Integer, List<Annotation>> map = new HashMap<>();
        scanner.scan(
                (index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
                AnnotationForScannerTest3.class, null
        );

        Assert.equals(3, map.size());
        Assert.equals(1, map.get(0).size());
        Assert.equals(AnnotationForScannerTest2.class, map.get(0).get(0).annotationType());

        Assert.equals(1, map.get(1).size());
        Assert.equals(AnnotationForScannerTest1.class, map.get(1).get(0).annotationType());

        Assert.equals(1, map.get(2).size());
        Assert.equals(AnnotationForScannerTest.class, map.get(2).get(0).annotationType());
    }

    static class Example {
        private Integer id;

        public Integer getId() {
            return id;
        }
    }

    @AnnotationForScannerTest
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @interface AnnotationForScannerTest1 {
    }

    @AnnotationForScannerTest1
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @interface AnnotationForScannerTest2 {
    }

    @AnnotationForScannerTest2
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @interface AnnotationForScannerTest3 {
    }
}
