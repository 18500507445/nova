package com.nova.tools.utils.hutool.core.map;

import cn.hutool.core.map.CamelCaseLinkedMap;
import cn.hutool.core.map.CamelCaseMap;
import cn.hutool.core.util.SerializeUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class CamelCaseMapTest {

	@Test
	public void caseInsensitiveMapTest() {
		CamelCaseMap<String, String> map = new CamelCaseMap<>();
		map.put("customKey", "OK");
		Assert.equals("OK", map.get("customKey"));
		Assert.equals("OK", map.get("custom_key"));
	}

	@Test
	public void caseInsensitiveLinkedMapTest() {
		CamelCaseLinkedMap<String, String> map = new CamelCaseLinkedMap<>();
		map.put("customKey", "OK");
		Assert.equals("OK", map.get("customKey"));
		Assert.equals("OK", map.get("custom_key"));
	}

	@Test
	public void serializableKeyFuncTest() {
		CamelCaseMap<String, String> map = new CamelCaseMap<>();
		map.put("serializable_key", "OK");
		CamelCaseMap<String, String> deSerializableMap = SerializeUtil.deserialize(SerializeUtil.serialize(map));
		Assert.equals("OK", deSerializableMap.get("serializable_key"));
		Assert.equals("OK", deSerializableMap.get("serializableKey"));
		deSerializableMap.put("serializable_func", "OK");
		Assert.equals("OK", deSerializableMap.get("serializable_func"));
		Assert.equals("OK", deSerializableMap.get("serializableFunc"));
	}


}
