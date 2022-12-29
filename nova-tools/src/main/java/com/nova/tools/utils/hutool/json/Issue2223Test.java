package com.nova.tools.utils.hutool.json;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Issue2223Test {

	@Test
	public void toStrOrderTest() {
		Map<String, Long> m1 = new LinkedHashMap<>();
		for (long i = 0; i < 5; i++) {
			m1.put("2022/" + i, i);
		}

		Assert.equals("{\"2022/0\":0,\"2022/1\":1,\"2022/2\":2,\"2022/3\":3,\"2022/4\":4}", JSONUtil.toJsonStr(m1));

		Map<String, Map<String, Long>> map1 = new HashMap<>();
		map1.put("m1", m1);

		Assert.equals("{\"m1\":{\"2022/0\":0,\"2022/1\":1,\"2022/2\":2,\"2022/3\":3,\"2022/4\":4}}",
				JSONUtil.toJsonStr(map1, JSONConfig.create()));

		final BeanDemo beanDemo = new BeanDemo();
		beanDemo.setMap1(map1);
		Assert.equals("{\"map1\":{\"m1\":{\"2022/0\":0,\"2022/1\":1,\"2022/2\":2,\"2022/3\":3,\"2022/4\":4}}}",
				JSONUtil.toJsonStr(beanDemo, JSONConfig.create()));
	}

	@Data
	static class BeanDemo {
		Map<String, Map<String, Long>> map1;
	}
}
