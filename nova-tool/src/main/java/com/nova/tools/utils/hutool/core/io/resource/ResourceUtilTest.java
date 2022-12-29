package com.nova.tools.utils.hutool.core.io.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.*;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class ResourceUtilTest {

	@Test
	public void readXmlTest(){
		final String str = ResourceUtil.readUtf8Str("test.xml");
		Assert.notNull(str);

		Resource resource = new ClassPathResource("test.xml");
		final String xmlStr = resource.readUtf8Str();

		Assert.equals(str, xmlStr);
	}

	@Test
	public void stringResourceTest(){
		final StringResource stringResource = new StringResource("testData", "test");
		Assert.equals("test", stringResource.getName());
		Assert.equals("testData".getBytes(), stringResource.readBytes());
		Assert.equals("testData".getBytes(), IoUtil.readBytes(stringResource.getStream()));
	}

	@Test
	public void fileResourceTest(){
		final FileResource resource = new FileResource(FileUtil.file("test.xml"));
		Assert.equals("test.xml", resource.getName());
		Assert.isTrue(StrUtil.isNotEmpty(resource.readUtf8Str()));
	}
}
