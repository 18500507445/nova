package com.nova.tools.utils.hutool.json;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class Issue1075Test {

	final String jsonStr = "{\"f1\":\"f1\",\"f2\":\"f2\",\"fac\":\"fac\"}";

	@Test
	public void testToBean() {
		// 在不忽略大小写的情况下，f2、fac都不匹配
		ObjA o2 = JSONUtil.toBean(jsonStr, ObjA.class);
		Assert.isNull(o2.getFAC());
		Assert.isNull(o2.getF2());
	}

	@Test
	public void testToBeanIgnoreCase() {
		// 在忽略大小写的情况下，f2、fac都匹配
		ObjA o2 = JSONUtil.parseObj(jsonStr, JSONConfig.create().setIgnoreCase(true)).toBean(ObjA.class);

		Assert.equals("fac", o2.getFAC());
		Assert.equals("f2", o2.getF2());
	}

	@Data
	public static class ObjA {
		private String f1;
		private String F2;
		private String FAC;
	}
}
