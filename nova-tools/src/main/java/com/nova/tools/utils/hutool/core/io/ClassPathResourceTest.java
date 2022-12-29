package com.nova.tools.utils.hutool.core.io;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * ClassPath资源读取测试
 *
 * @author Looly
 */
public class ClassPathResourceTest {

	@Test
	public void readStringTest() {
		ClassPathResource resource = new ClassPathResource("test.properties");
		String content = resource.readUtf8Str();
		Assert.isTrue(StrUtil.isNotEmpty(content));
	}

	@Test
	public void readStringTest2() {
		// 读取classpath根目录测试
		ClassPathResource resource = new ClassPathResource("/");
		String content = resource.readUtf8Str();
		Assert.isTrue(StrUtil.isNotEmpty(content));
	}

	@Test
	public void readTest() throws IOException {
		ClassPathResource resource = new ClassPathResource("test.properties");
		Properties properties = new Properties();
		properties.load(resource.getStream());

		Assert.equals("1", properties.get("a"));
		Assert.equals("2", properties.get("b"));
	}

	@Test
	public void readFromJarTest() {
		//测试读取junit的jar包下的LICENSE-junit.txt文件
		final ClassPathResource resource = new ClassPathResource("LICENSE-junit.txt");

		String result = resource.readUtf8Str();
		Assert.notNull(result);

		//二次读取测试，用于测试关闭流对再次读取的影响
		result = resource.readUtf8Str();
		Assert.notNull(result);
	}

	@Test
	public void getAbsTest() {
		final ClassPathResource resource = new ClassPathResource("LICENSE-junit.txt");
		String absPath = resource.getAbsolutePath();
		Assert.isTrue(absPath.contains("LICENSE-junit.txt"));
	}
}
