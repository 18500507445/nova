package com.nova.tools.utils.hutool.core.annotation.scanner;

import cn.hutool.core.annotation.scanner.AnnotationScanner;
import cn.hutool.core.annotation.scanner.TypeAnnotationScanner;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeAnnotationScannerTest {

	@Test
	public void supportTest() {
		AnnotationScanner scanner = new TypeAnnotationScanner();
		Assert.isTrue(scanner.support(Example.class));
		Assert.isFalse(scanner.support(ReflectUtil.getField(Example.class, "id")));
		Assert.isFalse(scanner.support(ReflectUtil.getMethod(Example.class, "getId")));
		Assert.isFalse(scanner.support(null));
	}

	@Test
	public void getAnnotationsTest() {
		AnnotationScanner scanner = new TypeAnnotationScanner();
		List<Annotation> annotations = scanner.getAnnotations(Example.class);
		Assert.equals(3, annotations.size());
		annotations.forEach(a -> Assert.equals(a.annotationType(), AnnotationForScannerTest.class));

		// 不查找父接口
		scanner = new TypeAnnotationScanner().setIncludeInterfaces(false);
		annotations = scanner.getAnnotations(Example.class);
		Assert.equals(2, annotations.size());
		annotations.forEach(a -> Assert.equals(a.annotationType(), AnnotationForScannerTest.class));

		// 不查找父类
		scanner = new TypeAnnotationScanner().setIncludeSuperClass(false);
		annotations = scanner.getAnnotations(Example.class);
		Assert.equals(1, annotations.size());
		annotations.forEach(a -> Assert.equals(a.annotationType(), AnnotationForScannerTest.class));

		// 不查找ExampleSupplerClass.class
		scanner = new TypeAnnotationScanner().addExcludeTypes(ExampleSupplerClass.class);
		annotations = scanner.getAnnotations(Example.class);
		Assert.equals(1, annotations.size());
		annotations.forEach(a -> Assert.equals(a.annotationType(), AnnotationForScannerTest.class));

		// 只查找ExampleSupplerClass.class
		scanner = new TypeAnnotationScanner().setFilter(t -> ClassUtil.isAssignable(ExampleSupplerClass.class, t));
		annotations = scanner.getAnnotations(Example.class);
		Assert.equals(2, annotations.size());
		annotations.forEach(a -> Assert.equals(a.annotationType(), AnnotationForScannerTest.class));
	}

	@Test
	public void scanTest() {
		Map<Integer, List<Annotation>> map = new HashMap<>();

		// 查找父类与父接口
		new TypeAnnotationScanner().scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			Example.class, null
		);
		Assert.equals(3, map.size());
		Assert.equals(1, map.get(0).size());
		Assert.equals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		Assert.equals(1, map.get(1).size());
		Assert.equals("ExampleSupplerClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
		Assert.equals(1, map.get(2).size());
		Assert.equals("ExampleInterface", ((AnnotationForScannerTest) map.get(2).get(0)).value());

		// 不查找父接口
		map.clear();
		new TypeAnnotationScanner()
			.setIncludeInterfaces(false)
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				Example.class, null
			);
		Assert.equals(2, map.size());
		Assert.equals(1, map.get(0).size());
		Assert.equals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		Assert.equals(1, map.get(1).size());
		Assert.equals("ExampleSupplerClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());

		// 不查找父类
		map.clear();
		new TypeAnnotationScanner()
			.setIncludeSuperClass(false)
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				Example.class, null
			);
		Assert.equals(1, map.size());
		Assert.equals(1, map.get(0).size());
		Assert.equals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());

		// 不查找ExampleSupplerClass.class
		map.clear();
		new TypeAnnotationScanner()
			.addExcludeTypes(ExampleSupplerClass.class)
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				Example.class, null
			);
		Assert.equals(1, map.size());
		Assert.equals(1, map.get(0).size());
		Assert.equals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());

		// 只查找ExampleSupplerClass.class
		map.clear();
		new TypeAnnotationScanner()
			.setFilter(t -> ClassUtil.isAssignable(ExampleSupplerClass.class, t))
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				Example.class, null
			);
		Assert.equals(2, map.size());
		Assert.equals(1, map.get(0).size());
		Assert.equals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		Assert.equals(1, map.get(1).size());
		Assert.equals("ExampleSupplerClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
	}

	@AnnotationForScannerTest("ExampleSupplerClass")
	static class ExampleSupplerClass implements ExampleInterface {}

	@AnnotationForScannerTest("ExampleInterface")
	interface ExampleInterface {}

	@AnnotationForScannerTest("Example")
	static class Example extends ExampleSupplerClass {
		private Integer id;
		public Integer getId() {
			return id;
		}
	}

}
