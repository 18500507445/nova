package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.lang.Assert;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * {@link RandomUtil} 随机工具类
 *
 * @author
 */
public class RandomUtilTest {

	@Test
	public void randomEleSetTest(){
		Set<Integer> set = RandomUtil.randomEleSet(CollUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
		Assert.equals(set.size(), 2);
	}

	@Test
	public void randomElesTest(){
		List<Integer> result = RandomUtil.randomEles(CollUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
		Assert.equals(result.size(), 2);
	}

	@Test
	public void randomDoubleTest() {
		double randomDouble = RandomUtil.randomDouble(0, 1, 0, RoundingMode.HALF_UP);
		Assert.isTrue(randomDouble <= 1);
	}

	@Test
	@Ignore
	public void randomBooleanTest() {
		Console.log(RandomUtil.randomBoolean());
	}

	@Test
	public void randomNumberTest() {
		final char c = RandomUtil.randomNumber();
		Assert.isTrue(c <= '9');
	}

	@Test
	public void randomIntTest() {
		final int c = RandomUtil.randomInt(10, 100);
		Assert.isTrue(c >= 10 && c < 100);
	}

	@Test
	public void randomBytesTest() {
		final byte[] c = RandomUtil.randomBytes(10);
		Assert.notNull(c);
	}

	@Test
	public void randomChineseTest(){
		char c = RandomUtil.randomChinese();
		Assert.isTrue(c > 0);
	}

	@Test
	@Ignore
	public void randomStringWithoutStrTest() {
		for (int i = 0; i < 100; i++) {
			final String s = RandomUtil.randomStringWithoutStr(8, "0IPOL");
			System.out.println(s);
			for (char c : "0IPOL".toCharArray()) {
				Assert.isFalse(s.contains((String.valueOf(c).toLowerCase(Locale.ROOT))));
			}
		}
	}
}
