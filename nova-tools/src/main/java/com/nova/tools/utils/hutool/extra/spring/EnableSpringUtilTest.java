package com.nova.tools.utils.hutool.extra.spring;

import cn.hutool.extra.spring.EnableSpringUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
/**
 * @author sidian
 */
@SpringBootTest(classes = EnableSpringUtilTest.class)
@EnableSpringUtil
public class EnableSpringUtilTest {

	@Test
	public void test() {
		// 使用@EnableSpringUtil注解后, 能获取上下文
		Assert.notNull(SpringUtil.getApplicationContext());
		// 不使用时, 为null
//        Assert.isNull(SpringUtil.getApplicationContext());
	}
}
