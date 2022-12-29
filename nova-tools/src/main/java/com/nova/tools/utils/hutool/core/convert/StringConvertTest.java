package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

public class StringConvertTest {

	@Test
	public void timezoneToStrTest(){
		final String s = Convert.toStr(TimeZone.getTimeZone("Asia/Shanghai"));
		Assert.equals("Asia/Shanghai", s);
	}
}
