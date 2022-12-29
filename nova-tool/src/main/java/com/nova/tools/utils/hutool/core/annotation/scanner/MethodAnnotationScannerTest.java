package com.nova.tools.utils.hutool.core.annotation.scanner;

import cn.hutool.core.annotation.scanner.AnnotationScanner;
import cn.hutool.core.annotation.scanner.MethodAnnotationScanner;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class MethodAnnotationScannerTest {

	@Test
	public void supportTest() {
		AnnotationScanner scanner = new MethodAnnotationScanner();
		Assert.isTrue(scanner.support(ReflectUtil.getMethod(Example.class, "test")));
		Assert.isFalse(scanner.support(null));
		Assert.isFalse(scanner.support(Example.class));
		Assert.isFalse(scanner.support(ReflectUtil.getField(Example.class, "id")));
	}

	@Test
	public void getAnnotationsTest() {
		AnnotationScanner scanner = new MethodAnnotationScanner();
		Method method = ReflectUtil.getMethod(Example.class, "test");
		Assert.notNull(method);

		// 不查找父类中具有相同方法签名的方法
		List<Annotation> annotations = scanner.getAnnotations(method);
		Assert.equals(1, annotations.size());
		Assert.equals(CollUtil.getFirst(annotations).annotationType(), AnnotationForScannerTest.class);

		// 查找父类中具有相同方法签名的方法
		scanner = new MethodAnnotationScanner(true);
		annotations = scanner.getAnnotations(method);
		Assert.equals(3, annotations.size());
		Assert.equals("Example", ((AnnotationForScannerTest) annotations.get(0)).value());
		Assert.equals("SuperClass", ((AnnotationForScannerTest) annotations.get(1)).value());
		Assert.equals("SuperInterface", ((AnnotationForScannerTest) annotations.get(2)).value());

		// 查找父类中具有相同方法签名的方法，但是不查找SuperInterface
		scanner = new MethodAnnotationScanner(true).addExcludeTypes(SuperInterface.class);
		annotations = scanner.getAnnotations(method);
		Assert.equals(2, annotations.size());
		Assert.equals("Example", ((AnnotationForScannerTest) annotations.get(0)).value());
		Assert.equals("SuperClass", ((AnnotationForScannerTest) annotations.get(1)).value());

		// 查找父类中具有相同方法签名的方法，但是只查找SuperClass
		scanner = new MethodAnnotationScanner(true)
			.setFilter(t -> ClassUtil.isAssignable(SuperClass.class, t));
		annotations = scanner.getAnnotations(method);
		Assert.equals(2, annotations.size());
		Assert.equals("Example", ((AnnotationForScannerTest) annotations.get(0)).value());
		Assert.equals("SuperClass", ((AnnotationForScannerTest) annotations.get(1)).value());
	}

	@Test
	public void scanTest() {
		Method method = ReflectUtil.getMethod(Example.class, "test");

		// 不查找父类中具有相同方法签名的方法
		Map<Integer, List<Annotation>> map = new HashMap<>();
		new MethodAnnotationScanner(false).scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			method, null
		);
		Assert.equals(1, map.get(0).size());
		Assert.equals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());

		// 查找父类中具有相同方法签名的方法
		map.clear();
		new MethodAnnotationScanner(true).scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			method, null
		);
		Assert.equals(3, map.size());
		Assert.equals(1, map.get(0).size());
		Assert.equals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		Assert.equals(1, map.get(1).size());
		Assert.equals("SuperClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
		Assert.equals(1, map.get(2).size());
		Assert.equals("SuperInterface", ((AnnotationForScannerTest) map.get(2).get(0)).value());

		// 查找父类中具有相同方法签名的方法，但是不查找SuperInterface
		map.clear();
		new MethodAnnotationScanner(true)
			.addExcludeTypes(SuperInterface.class)
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				method, null
			);
		Assert.equals(2, map.size());
		Assert.equals(1, map.get(0).size());
		Assert.equals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		Assert.equals(1, map.get(1).size());
		Assert.equals("SuperClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());

		// 查找父类中具有相同方法签名的方法，但是只查找SuperClass
		map.clear();
		new MethodAnnotationScanner(true)
			.setFilter(t -> ClassUtil.isAssignable(SuperClass.class, t))
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				method, null
			);
		Assert.equals(2, map.size());
		Assert.equals(1, map.get(0).size());
		Assert.equals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		Assert.equals(1, map.get(1).size());
		Assert.equals("SuperClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
	}

	static class Example extends SuperClass {
		private Integer id;

		@Override
		@AnnotationForScannerTest("Example")
		public List<?> test() { return Collections.emptyList(); }
	}

	static class SuperClass implements SuperInterface {

		@Override
		@AnnotationForScannerTest("SuperClass")
		public Collection<?> test() { return Collections.emptyList(); }

	}

	interface SuperInterface {

		@AnnotationForScannerTest("SuperInterface")
		Object test();

	}

}
