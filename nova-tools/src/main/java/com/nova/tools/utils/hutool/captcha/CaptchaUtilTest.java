package cn.hutool.captcha;

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
}
