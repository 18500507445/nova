package com.nova.tools.utils.hutool.core.collection;

import cn.hutool.core.collection.UniqueKeySet;
import lombok.AllArgsConstructor;
import lombok.Data;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class UniqueKeySetTest {

	@Test
	public void addTest(){
		Set<UniqueTestBean> set = new UniqueKeySet<>(UniqueTestBean::getId);
		set.add(new UniqueTestBean("id1", "张三", "地球"));
		set.add(new UniqueTestBean("id2", "李四", "火星"));
		// id重复，替换之前的元素
		set.add(new UniqueTestBean("id2", "王五", "木星"));

		// 后两个ID重复
		Assert.equals(2, set.size());
	}

	@Data
	@AllArgsConstructor
	static class UniqueTestBean{
		private String id;
		private String name;
		private String address;
	}
}
