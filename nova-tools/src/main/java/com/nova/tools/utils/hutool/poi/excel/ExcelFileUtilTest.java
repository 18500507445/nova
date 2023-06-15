package com.nova.tools.utils.hutool.poi.excel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelFileUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class ExcelFileUtilTest {

    @Test
    public void xlsTest() {
        InputStream in = FileUtil.getInputStream("aaa.xls");
        try {
            Assert.isTrue(ExcelFileUtil.isXls(in));
            Assert.isFalse(ExcelFileUtil.isXlsx(in));
        } finally {
            IoUtil.close(in);
        }
    }

    @Test
    public void xlsxTest() {
        InputStream in = FileUtil.getInputStream("aaa.xlsx");
        try {
            Assert.isFalse(ExcelFileUtil.isXls(in));
            Assert.isTrue(ExcelFileUtil.isXlsx(in));
        } finally {
            IoUtil.close(in);
        }
    }
}
