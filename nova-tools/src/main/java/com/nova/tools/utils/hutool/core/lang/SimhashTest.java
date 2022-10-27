package com.nova.tools.utils.hutool.core.lang;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.Simhash;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

public class SimhashTest {
	
	@Test
	public void simTest() {
		String text1 = "我是 一个 普通 字符串";
		String text2 = "我是 一个 普通 字符串";
		
		Simhash simhash = new Simhash();
		long hash = simhash.hash(StrUtil.split(text1, ' '));
		Assert.isTrue(hash != 0);
		
		simhash.store(hash);
		boolean duplicate = simhash.equals(StrUtil.split(text2, ' '));
		Assert.isTrue(duplicate);
	}
}
