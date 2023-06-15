package com.nova.tools.utils.hutool.core.text.csv;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.CsvParser;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class CsvParserTest {

    @Test
    public void parseTest1() {
        StringReader reader = StrUtil.getReader("aaa,b\"bba\",ccc");
        CsvParser parser = new CsvParser(reader, null);
        CsvRow row = parser.nextRow();
        //noinspection ConstantConditions
        Assert.equals("b\"bba\"", row.getRawList().get(1));
        IoUtil.close(parser);
    }

    @Test
    public void parseTest2() {
        StringReader reader = StrUtil.getReader("aaa,\"bba\"bbb,ccc");
        CsvParser parser = new CsvParser(reader, null);
        CsvRow row = parser.nextRow();
        //noinspection ConstantConditions
        Assert.equals("\"bba\"bbb", row.getRawList().get(1));
        IoUtil.close(parser);
    }

    @Test
    public void parseTest3() {
        StringReader reader = StrUtil.getReader("aaa,\"bba\",ccc");
        CsvParser parser = new CsvParser(reader, null);
        CsvRow row = parser.nextRow();
        //noinspection ConstantConditions
        Assert.equals("bba", row.getRawList().get(1));
        IoUtil.close(parser);
    }

    @Test
    public void parseTest4() {
        StringReader reader = StrUtil.getReader("aaa,\"\",ccc");
        CsvParser parser = new CsvParser(reader, null);
        CsvRow row = parser.nextRow();
        //noinspection ConstantConditions
        Assert.equals("", row.getRawList().get(1));
        IoUtil.close(parser);
    }

    @Test
    public void parseEscapeTest() {
        // https://datatracker.ietf.org/doc/html/rfc4180#section-2
        // 第七条规则
        StringReader reader = StrUtil.getReader("\"b\"\"bb\"");
        CsvParser parser = new CsvParser(reader, null);
        CsvRow row = parser.nextRow();
        Assert.notNull(row);
        Assert.equals(1, row.size());
        Assert.equals("b\"bb", row.get(0));
    }
}
