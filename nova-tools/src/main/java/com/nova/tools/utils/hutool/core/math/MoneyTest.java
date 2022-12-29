package com.nova.tools.utils.hutool.core.math;

import cn.hutool.core.math.MathUtil;
import cn.hutool.core.math.Money;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class MoneyTest {

	@Test
	public void yuanToCentTest() {
		final Money money = new Money("1234.56");
		Assert.equals(123456, money.getCent());

		Assert.equals(123456, MathUtil.yuanToCent(1234.56));
	}

	@Test
	public void centToYuanTest() {
		final Money money = new Money(1234, 56);
		Assert.equals(1234.56D, money.getAmount().doubleValue());

		Assert.equals(1234.56D, MathUtil.centToYuan(123456));
	}
}
