package com.nova.tools.utils.hutool.crypto.symmetric;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class PBKDF2Test {

	@Test
	public void encryptTest(){
		final String s = SecureUtil.pbkdf2("123456".toCharArray(), RandomUtil.randomBytes(16));
		Assert.equals(128, s.length());
	}
}
