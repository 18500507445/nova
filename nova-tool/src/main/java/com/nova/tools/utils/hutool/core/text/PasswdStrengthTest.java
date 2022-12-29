package com.nova.tools.utils.hutool.core.text;

import cn.hutool.core.text.PasswdStrength;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class PasswdStrengthTest {
	@Test
	public void strengthTest(){
		String passwd = "2hAj5#mne-ix.86H";
		Assert.equals(13, PasswdStrength.check(passwd));
	}

	@Test
	public void strengthNumberTest(){
		String passwd = "9999999999999";
		Assert.equals(0, PasswdStrength.check(passwd));
	}
}
