package com.nova.tools.utils.hutool.core.date;

import cn.hutool.core.date.ZoneUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.TimeZone;

public class ZoneUtilTest {
	@Test
	public void toTest() {
		Assert.equals(ZoneId.systemDefault(), ZoneUtil.toZoneId(null));
		Assert.equals(TimeZone.getDefault(), ZoneUtil.toTimeZone(null));
	}
}
