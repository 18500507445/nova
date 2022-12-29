package com.nova.tools.utils.hutool.poi;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class IssueI5Q1TWTest {

	@Test
	public void readTest() {
		final ExcelReader reader = ExcelUtil.getReader("I5Q1TW.xlsx");

		// 自定义时间格式1
		Assert.equals("18:56", reader.readCellValue(0, 0).toString());

		// 自定义时间格式2
		Assert.equals("18:56", reader.readCellValue(1, 0).toString());
	}
}
