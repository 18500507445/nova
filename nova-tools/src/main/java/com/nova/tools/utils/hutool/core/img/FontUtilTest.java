package com.nova.tools.utils.hutool.core.img;

import cn.hutool.core.img.FontUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.awt.Font;

public class FontUtilTest {

	@Test
	public void createFontTest(){
		final Font font = FontUtil.createFont();
		Assert.notNull(font);
	}
}
