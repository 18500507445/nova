package com.nova.tools.utils.hutool.json;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * JSON路径单元测试
 *
 * @author looly
 *
 */
public class JSONPathTest {

	@Test
	public void getByPathTest() {
		String json = "[{\"id\":\"1\",\"name\":\"xingming\"},{\"id\":\"2\",\"name\":\"mingzi\"}]";
		Object value = JSONUtil.parseArray(json).getByPath("[0].name");
		Assert.equals("xingming", value);
		value = JSONUtil.parseArray(json).getByPath("[1].name");
		Assert.equals("mingzi", value);
	}

	@Test
	public void getByPathTest2(){
		String str = "{'accountId':111}";
		JSON json = JSONUtil.parse(str);
		Long accountId = JSONUtil.getByPath(json, "$.accountId", 0L);
		Assert.equals(111L, accountId.longValue());
	}
}
