package com.nova.tools.utils.hutool.extra.pinyin;

import cn.hutool.extra.pinyin.engine.jpinyin.JPinyinEngine;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class JpinyinTest {

	final JPinyinEngine engine = new JPinyinEngine();

	@Test
	public void getFirstLetterByPinyin4jTest(){
		final String result = engine.getFirstLetter("林海", "");
		Assert.equals("lh", result);
	}

	@Test
	public void getPinyinByPinyin4jTest() {
		final String pinyin = engine.getPinyin("你好h", " ");
		Assert.equals("ni hao h", pinyin);
	}

}
