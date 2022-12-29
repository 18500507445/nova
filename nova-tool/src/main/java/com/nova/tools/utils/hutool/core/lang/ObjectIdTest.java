package com.nova.tools.utils.hutool.core.lang;

import java.util.HashSet;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.lang.Assert;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

/**
 * ObjectId单元测试
 * 
 * @author looly
 *
 */
public class ObjectIdTest {
	
	@Test
	public void distinctTest() {
		//生成10000个id测试是否重复
		HashSet<String> set = new HashSet<>();
		for(int i = 0; i < 10000; i++) {
			set.add(ObjectId.next());
		}
		
		Assert.equals(10000, set.size());
	}
	
	@Test
	@Ignore
	public void nextTest() {
		Console.log(ObjectId.next());
	}
}
