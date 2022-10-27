package com.nova.tools.utils.hutool.core.codec;

import cn.hutool.core.codec.Base62;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * Base62单元测试
 *
 * @author looly
 *
 */
public class Base62Test {

	@Test
	public void encodeAndDecodeTest() {
		String a = "伦家是一个非常长的字符串66";
		String encode = Base62.encode(a);
		Assert.equals("17vKU8W4JMG8dQF8lk9VNnkdMOeWn4rJMva6F0XsLrrT53iKBnqo", encode);

		String decodeStr = Base62.decodeStr(encode);
		Assert.equals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeInvertedTest() {
		String a = "伦家是一个非常长的字符串66";
		String encode = Base62.encodeInverted(a);
		Assert.equals("17Vku8w4jmg8Dqf8LK9vnNKDmoEwN4RjmVA6f0xSlRRt53IkbNQO", encode);

		String decodeStr = Base62.decodeStrInverted(encode);
		Assert.equals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeRandomTest() {
		String a = RandomUtil.randomString(RandomUtil.randomInt(1000));
		String encode = Base62.encode(a);
		String decodeStr = Base62.decodeStr(encode);
		Assert.equals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeInvertedRandomTest() {
		String a = RandomUtil.randomString(RandomUtil.randomInt(1000));
		String encode = Base62.encodeInverted(a);
		String decodeStr = Base62.decodeStrInverted(encode);
		Assert.equals(a, decodeStr);
	}
}
