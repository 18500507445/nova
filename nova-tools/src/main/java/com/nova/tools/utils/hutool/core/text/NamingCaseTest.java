package com.nova.tools.utils.hutool.core.text;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class NamingCaseTest {

	@Test
	public void toCamelCaseTest() {
		Dict.create()
				.set("Table_Test_Of_day","tableTestOfDay")
				.set("TableTestOfDay","TableTestOfDay")
				.set("abc_1d","abc1d")
				.forEach((key, value) -> Assert.equals(value, NamingCase.toCamelCase(key)));
	}

	@Test
	public void toCamelCaseFromDashedTest() {
		Dict.create()
				.set("Table-Test-Of-day","tableTestOfDay")
				.forEach((key, value) -> Assert.equals(value, NamingCase.toCamelCase(key, CharUtil.DASHED)));
	}

	@Test
	public void toUnderLineCaseTest() {
		Dict.create()
				.set("Table_Test_Of_day", "table_test_of_day")
				.set("_Table_Test_Of_day_", "_table_test_of_day_")
				.set("_Table_Test_Of_DAY_", "_table_test_of_DAY_")
				.set("_TableTestOfDAYToday", "_table_test_of_DAY_today")
				.set("HelloWorld_test", "hello_world_test")
				.set("H2", "H2")
				.set("H#case", "H#case")
				.set("PNLabel", "PN_label")
				.set("wPRunOZTime", "w_P_run_OZ_time")
				// https://github.com/dromara/hutool/issues/2070
				.set("customerNickV2", "customer_nick_v2")
				// https://gitee.com/dromara/hutool/issues/I4X9TT
				.set("DEPT_NAME","DEPT_NAME")
				.forEach((key, value) -> Assert.equals(value, NamingCase.toUnderlineCase(key)));
	}
}
