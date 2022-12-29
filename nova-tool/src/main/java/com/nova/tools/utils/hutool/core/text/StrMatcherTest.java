package com.nova.tools.utils.hutool.core.text;

import cn.hutool.core.text.StrMatcher;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class StrMatcherTest {

	@Test
	public void matcherTest(){
		final StrMatcher strMatcher = new StrMatcher("${name}-${age}-${gender}-${country}-${province}-${city}-${status}");
		final Map<String, String> match = strMatcher.match("小明-19-男-中国-河南-郑州-已婚");
		Assert.equals("小明", match.get("name"));
		Assert.equals("19", match.get("age"));
		Assert.equals("男", match.get("gender"));
		Assert.equals("中国", match.get("country"));
		Assert.equals("河南", match.get("province"));
		Assert.equals("郑州", match.get("city"));
		Assert.equals("已婚", match.get("status"));
	}

	@Test
	public void matcherTest2(){
		// 当有无匹配项的时候，按照全不匹配对待
		final StrMatcher strMatcher = new StrMatcher("${name}-${age}-${gender}-${country}-${province}-${city}-${status}");
		final Map<String, String> match = strMatcher.match("小明-19-男-中国-河南-郑州");
		Assert.equals(0, match.size());
	}

	@Test
	public void matcherTest3(){
		// 当有无匹配项的时候，按照全不匹配对待
		final StrMatcher strMatcher = new StrMatcher("${name}经过${year}年");
		final Map<String, String> match = strMatcher.match("小明经过20年，成长为一个大人。");
		//Console.log(match);
		Assert.equals("小明", match.get("name"));
		Assert.equals("20", match.get("year"));
	}
}
