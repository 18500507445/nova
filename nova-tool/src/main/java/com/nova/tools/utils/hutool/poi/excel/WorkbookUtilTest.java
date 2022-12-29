package com.nova.tools.utils.hutool.poi.excel;

import cn.hutool.poi.excel.WorkbookUtil;
import org.apache.poi.ss.usermodel.Workbook;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class WorkbookUtilTest {

	@Test
	public void createBookTest(){
		Workbook book = WorkbookUtil.createBook(true);
		Assert.notNull(book);

		book = WorkbookUtil.createBook(false);
		Assert.notNull(book);
	}
}
