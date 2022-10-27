package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class ClassLoaderUtilTest {
	
	@Test
	public void loadClassTest() {
		String name = ClassLoaderUtil.loadClass("java.lang.Thread.State").getName();
		Assert.equals("java.lang.Thread$State", name);
		
		name = ClassLoaderUtil.loadClass("java.lang.Thread$State").getName();
		Assert.equals("java.lang.Thread$State", name);
	}
}
