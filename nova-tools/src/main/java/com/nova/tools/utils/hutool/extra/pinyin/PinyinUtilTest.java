package com.nova.tools.utils.hutool.extra.pinyin;

import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class PinyinUtilTest {

	@Test
	public void getPinyinTest(){
		final String pinyin = PinyinUtil.getPinyin("你好怡", " ");
		Assert.equals("ni hao yi", pinyin);
	}

	@Test
	public void getFirstLetterTest(){
		final String result = PinyinUtil.getFirstLetter("H是第一个", ", ");
		Assert.equals("h, s, d, y, g", result);
	}

	@Test
	public void getFirstLetterTest2(){
		final String result = PinyinUtil.getFirstLetter("崞阳", ", ");
		Assert.equals("g, y", result);
	}
}
