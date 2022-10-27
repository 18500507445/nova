package com.nova.tools.utils.hutool.json;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * Predicate多层过滤
 */
public class IssueI5OMSCTest {

	@Test
	public void filterTest(){
		final JSONObject json = JSONUtil.parseObj(ResourceUtil.readUtf8Str("issueI5OMSC.json"));

		final String s = json.toJSONString(0, (entry) -> {
			final Object key = entry.getKey();
			if(key instanceof String){
				return ListUtil.of("store", "bicycle", "color", "book", "author").contains(key);
			}
			return true;
		});
		Assert.equals("{\"store\":{\"bicycle\":{\"color\":\"red\"},\"book\":[{\"author\":\"Evelyn Waugh\"},{\"author\":\"Evelyn Waugh02\"}]}}", s);
	}
}
