package com.nova.tools.utils.hutool.extra.pinyin;

import cn.hutool.extra.pinyin.engine.bopomofo4j.Bopomofo4jEngine;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class Bopomofo4jTest {

	final Bopomofo4jEngine engine = new Bopomofo4jEngine();

	@Test
	public void getFirstLetterByBopomofo4jTest(){
		final String result = engine.getFirstLetter("林海", "");
		Assert.equals("lh", result);
	}

	@Test
	public void getPinyinByBopomofo4jTest() {
		final String pinyin = engine.getPinyin("你好h", " ");
		Assert.equals("ni haoh", pinyin);
	}

}
