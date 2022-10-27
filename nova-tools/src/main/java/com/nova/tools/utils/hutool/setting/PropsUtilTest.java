package com.nova.tools.utils.hutool.setting;

import cn.hutool.setting.dialect.PropsUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class PropsUtilTest {

	@Test
	public void getTest() {
		String driver = PropsUtil.get("test").getStr("driver");
		Assert.equals("com.mysql.jdbc.Driver", driver);
	}

	@Test
	public void getFirstFoundTest() {
		String driver = Objects.requireNonNull(PropsUtil.getFirstFound("test2", "test")).getStr("driver");
		Assert.equals("com.mysql.jdbc.Driver", driver);
	}
}
