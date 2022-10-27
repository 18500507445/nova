package com.nova.tools.utils.hutool.core.lang;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import cn.hutool.core.collection.CollUtil;

public class WeightRandomTest {

	@Test
	public void weightRandomTest() {
		WeightRandom<String> random = WeightRandom.create();
		random.add("A", 10);
		random.add("B", 50);
		random.add("C", 100);

		String result = random.next();
		Assert.isTrue(CollUtil.newArrayList("A", "B", "C").contains(result));
	}
}
