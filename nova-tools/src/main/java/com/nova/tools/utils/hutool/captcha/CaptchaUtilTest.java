package com.nova.tools.utils.hutool.captcha;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.MathGenerator;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

public class CaptchaUtilTest {

	@Test
	@Ignore
	public void createTest() {
		for(int i = 0; i < 1; i++) {
			CaptchaUtil.createShearCaptcha(320, 240);
		}
	}

	@Test
	public void mathGeneratorTest() {
		final MathGenerator mathGenerator = new MathGenerator();
		for (int i = 0; i < 1000; i++) {
			mathGenerator.verify(mathGenerator.generate(), "0");
		}
	}
}
