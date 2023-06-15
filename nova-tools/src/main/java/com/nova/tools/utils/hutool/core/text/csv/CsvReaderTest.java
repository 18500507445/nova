package com.nova.tools.utils.hutool.core.text.csv;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.csv.*;
import cn.hutool.core.util.CharsetUtil;
import lombok.Data;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.List;
import java.util.Map;

public class CsvReaderTest {

    @Test
    public void readTest() {
        CsvReader reader = new CsvReader();
        CsvData data = reader.read(ResourceUtil.getReader("test.csv", CharsetUtil.CHARSET_UTF_8));
        Assert.equals("sss,sss", data.getRow(0).get(0));
        Assert.equals(1, data.getRow(0).getOriginalLineNumber());
        Assert.equals("性别", data.getRow(0).get(2));
        Assert.equals("关注\"对象\"", data.getRow(0).get(3));
    }

    @Test
    public void readMapListTest() {
        final CsvReader reader = CsvUtil.getReader();
        final List<Map<String, String>> result = reader.readMapList(
                ResourceUtil.getUtf8Reader("test_bean.csv"));

        Assert.equals("张三", result.get(0).get("姓名"));
        Assert.equals("男", result.get(0).get("gender"));
        Assert.equals("无", result.get(0).get("focus"));
        Assert.equals("33", result.get(0).get("age"));

        Assert.equals("李四", result.get(1).get("姓名"));
        Assert.equals("男", result.get(1).get("gender"));
        Assert.equals("好对象", result.get(1).get("focus"));
        Assert.equals("23", result.get(1).get("age"));

        Assert.equals("王妹妹", result.get(2).get("姓名"));
        Assert.equals("女", result.get(2).get("gender"));
        Assert.equals("特别关注", result.get(2).get("focus"));
        Assert.equals("22", result.get(2).get("age"));
    }

    @Test
    public void readAliasMapListTest() {
        final CsvReadConfig csvReadConfig = CsvReadConfig.defaultConfig();
        csvReadConfig.addHeaderAlias("姓名", "name");

        final CsvReader reader = CsvUtil.getReader(csvReadConfig);
        final List<Map<String, String>> result = reader.readMapList(
                ResourceUtil.getUtf8Reader("test_bean.csv"));

        Assert.equals("张三", result.get(0).get("name"));
        Assert.equals("男", result.get(0).get("gender"));
        Assert.equals("无", result.get(0).get("focus"));
        Assert.equals("33", result.get(0).get("age"));

        Assert.equals("李四", result.get(1).get("name"));
        Assert.equals("男", result.get(1).get("gender"));
        Assert.equals("好对象", result.get(1).get("focus"));
        Assert.equals("23", result.get(1).get("age"));

        Assert.equals("王妹妹", result.get(2).get("name"));
        Assert.equals("女", result.get(2).get("gender"));
        Assert.equals("特别关注", result.get(2).get("focus"));
        Assert.equals("22", result.get(2).get("age"));
    }

    @Test
    public void readBeanListTest() {
        final CsvReader reader = CsvUtil.getReader();
        final List<TestBean> result = reader.read(
                ResourceUtil.getUtf8Reader("test_bean.csv"), TestBean.class);

        Assert.equals("张三", result.get(0).getName());
        Assert.equals("男", result.get(0).getGender());
        Assert.equals("无", result.get(0).getFocus());
        Assert.equals(Integer.valueOf(33), result.get(0).getAge());

        Assert.equals("李四", result.get(1).getName());
        Assert.equals("男", result.get(1).getGender());
        Assert.equals("好对象", result.get(1).getFocus());
        Assert.equals(Integer.valueOf(23), result.get(1).getAge());

        Assert.equals("王妹妹", result.get(2).getName());
        Assert.equals("女", result.get(2).getGender());
        Assert.equals("特别关注", result.get(2).getFocus());
        Assert.equals(Integer.valueOf(22), result.get(2).getAge());
    }

    @Data
    private static class TestBean {
        @Alias("姓名")
        private String name;
        private String gender;
        private String focus;
        private Integer age;
    }

    @Test
    @Ignore
    public void readTest2() {
        final CsvReader reader = CsvUtil.getReader();
        final CsvData read = reader.read(FileUtil.file("d:/test/test.csv"));
        for (CsvRow strings : read) {
            Console.log(strings);
        }
    }

    @Test
    @Ignore
    public void readTest3() {
        final CsvReadConfig csvReadConfig = CsvReadConfig.defaultConfig();
        csvReadConfig.setContainsHeader(true);
        final CsvReader reader = CsvUtil.getReader(csvReadConfig);
        final CsvData read = reader.read(FileUtil.file("d:/test/ceshi.csv"));
        for (CsvRow row : read) {
            Console.log(row.getByName("案件ID"));
        }
    }

    @Test
    public void lineNoTest() {
        CsvReader reader = new CsvReader();
        CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.CHARSET_UTF_8));
        Assert.equals(1, data.getRow(0).getOriginalLineNumber());
        Assert.equals("a,b,c,d", CollUtil.join(data.getRow(0), ","));

        Assert.equals(4, data.getRow(2).getOriginalLineNumber());
        Assert.equals("q,w,e,r,我是一段\n带换行的内容",
                CollUtil.join(data.getRow(2), ",").replace("\r", ""));

        // 文件中第3行数据，对应原始行号是6（从0开始）
        Assert.equals(6, data.getRow(3).getOriginalLineNumber());
        Assert.equals("a,s,d,f", CollUtil.join(data.getRow(3), ","));
    }

    @Test
    public void lineLimitTest() {
        // 从原始第2行开始读取
        CsvReader reader = new CsvReader(CsvReadConfig.defaultConfig().setBeginLineNo(2));
        CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.CHARSET_UTF_8));

        Assert.equals(2, data.getRow(0).getOriginalLineNumber());
        Assert.equals("1,2,3,4", CollUtil.join(data.getRow(0), ","));

        Assert.equals(4, data.getRow(1).getOriginalLineNumber());
        Assert.equals("q,w,e,r,我是一段\n带换行的内容",
                CollUtil.join(data.getRow(1), ",").replace("\r", ""));

        // 文件中第3行数据，对应原始行号是6（从0开始）
        Assert.equals(6, data.getRow(2).getOriginalLineNumber());
        Assert.equals("a,s,d,f", CollUtil.join(data.getRow(2), ","));
    }

    @Test
    public void lineLimitWithHeaderTest() {
        // 从原始第2行开始读取
        CsvReader reader = new CsvReader(CsvReadConfig.defaultConfig().setBeginLineNo(2).setContainsHeader(true));
        CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.CHARSET_UTF_8));

        Assert.equals(4, data.getRow(0).getOriginalLineNumber());
        Assert.equals("q,w,e,r,我是一段\n带换行的内容",
                CollUtil.join(data.getRow(0), ",").replace("\r", ""));

        // 文件中第3行数据，对应原始行号是6（从0开始）
        Assert.equals(6, data.getRow(1).getOriginalLineNumber());
        Assert.equals("a,s,d,f", CollUtil.join(data.getRow(1), ","));
    }

    @Test
    public void customConfigTest() {
        final CsvReader reader = CsvUtil.getReader(
                CsvReadConfig.defaultConfig()
                        .setTextDelimiter('\'')
                        .setFieldSeparator(';'));
        final CsvData csvRows = reader.readFromStr("123;456;'789;0'abc;");
        final CsvRow row = csvRows.getRow(0);
        Assert.equals("123", row.get(0));
        Assert.equals("456", row.get(1));
        Assert.equals("'789;0'abc", row.get(2));
    }

    @Test
    public void readDisableCommentTest() {
        final CsvReader reader = CsvUtil.getReader(CsvReadConfig.defaultConfig().disableComment());
        final CsvData read = reader.read(ResourceUtil.getUtf8Reader("test.csv"));
        final CsvRow row = read.getRow(0);
        Assert.equals("# 这是一行注释，读取时应忽略", row.get(0));
    }

    @Test
    @Ignore
    public void streamTest() {
        final CsvReader reader = CsvUtil.getReader(ResourceUtil.getUtf8Reader("test_bean.csv"));
        reader.stream().limit(2).forEach(Console::log);
    }
}
