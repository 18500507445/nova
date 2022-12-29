package com.nova.tools.utils.hutool.setting.yaml;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.yaml.YamlUtil;
import cn.hutool.core.lang.Assert;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import java.util.List;

public class YamlUtilTest {

	@Test
	public void loadByPathTest() {
		final Dict result = YamlUtil.loadByPath("test.yaml");

		Assert.equals("John", result.getStr("firstName"));

		final List<Integer> numbers = result.getByPath("contactDetails.number");
		Assert.equals(123456789, (int) numbers.get(0));
		Assert.equals(456786868, (int) numbers.get(1));
	}

	@Test
	@Ignore
	public void dumpTest() {
		final Dict dict = Dict.create()
				.set("name", "hutool")
				.set("count", 1000);

		YamlUtil.dump(
				dict
				, FileUtil.getWriter("d:/test/dump.yaml", CharsetUtil.CHARSET_UTF_8, false));
	}
}
