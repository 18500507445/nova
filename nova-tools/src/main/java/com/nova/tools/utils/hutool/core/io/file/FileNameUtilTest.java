package com.nova.tools.utils.hutool.core.io.file;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class FileNameUtilTest {

	@Test
	public void cleanInvalidTest(){
		String name = FileNameUtil.cleanInvalid("1\n2\n");
		Assert.equals("12", name);

		name = FileNameUtil.cleanInvalid("\r1\r\n2\n");
		Assert.equals("12", name);
	}

	@Test
	public void mainNameTest() {
		final String s = FileNameUtil.mainName("abc.tar.gz");
		Assert.equals("abc", s);
	}
}
