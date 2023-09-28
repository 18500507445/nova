package com.nova.tools.utils.hutool.poi.excel;

import cn.hutool.core.lang.Assert;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.cell.CellLocation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ExcelUtilTest {

    @Test
    public void indexToColNameTest() {
        Assert.equals("A", ExcelUtil.indexToColName(0));
        Assert.equals("B", ExcelUtil.indexToColName(1));
        Assert.equals("C", ExcelUtil.indexToColName(2));

        Assert.equals("AA", ExcelUtil.indexToColName(26));
        Assert.equals("AB", ExcelUtil.indexToColName(27));
        Assert.equals("AC", ExcelUtil.indexToColName(28));

        Assert.equals("AAA", ExcelUtil.indexToColName(702));
        Assert.equals("AAB", ExcelUtil.indexToColName(703));
        Assert.equals("AAC", ExcelUtil.indexToColName(704));
    }

    @Test
    public void colNameToIndexTest() {
        Assert.equals(704, ExcelUtil.colNameToIndex("AAC"));
        Assert.equals(703, ExcelUtil.colNameToIndex("AAB"));
        Assert.equals(702, ExcelUtil.colNameToIndex("AAA"));

        Assert.equals(28, ExcelUtil.colNameToIndex("AC"));
        Assert.equals(27, ExcelUtil.colNameToIndex("AB"));
        Assert.equals(26, ExcelUtil.colNameToIndex("AA"));

        Assert.equals(2, ExcelUtil.colNameToIndex("C"));
        Assert.equals(1, ExcelUtil.colNameToIndex("B"));
        Assert.equals(0, ExcelUtil.colNameToIndex("A"));
    }

    @Test
    public void toLocationTest() {
        final CellLocation a11 = ExcelUtil.toLocation("A11");
        Assert.equals(0, a11.getX());
        Assert.equals(10, a11.getY());
    }

    @Test
    public void readAndWriteTest() {
        ExcelReader reader = ExcelUtil.getReader("aaa.xlsx");
        ExcelWriter writer = reader.getWriter();
        writer.writeCellValue(1, 2, "设置值");
        writer.close();
    }

    @Test
    public void getReaderByBookFilePathAndSheetNameTest() {
        ExcelReader reader = ExcelUtil.getReader("aaa.xlsx", "12");
        List<Map<String, Object>> list = reader.readAll();
        reader.close();
        Assert.equals(1L, list.get(1).get("鞋码"));
    }
}
