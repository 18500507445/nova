package com.nova.tools.utils.hutool.poi.excel;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.cell.CellHandler;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Excel读取单元测试
 *
 * @author:Looly
 */
public class ExcelReadTest {

    @Test
    public void aliasTest() {
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("alias.xlsx"));

        //读取单个单元格内容测试
        Object value = reader.readCellValue(1, 2);
        Assert.equals("仓库", value);

        Map<String, String> headerAlias = MapUtil.newHashMap();
        headerAlias.put("用户姓名", "userName");
        headerAlias.put("库房", "storageName");
        headerAlias.put("盘点权限", "checkPerm");
        headerAlias.put("领料审批权限", "allotAuditPerm");
        reader.setHeaderAlias(headerAlias);

        // 读取list时默认首个非空行为标题
        List<List<Object>> read = reader.read();
        Assert.equals("userName", read.get(0).get(0));
        Assert.equals("storageName", read.get(0).get(1));
        Assert.equals("checkPerm", read.get(0).get(2));
        Assert.equals("allotAuditPerm", read.get(0).get(3));

        List<Map<String, Object>> readAll = reader.readAll();
        for (Map<String, Object> map : readAll) {
            Assert.isTrue(map.containsKey("userName"));
            Assert.isTrue(map.containsKey("storageName"));
            Assert.isTrue(map.containsKey("checkPerm"));
            Assert.isTrue(map.containsKey("allotAuditPerm"));
        }
    }

    @Test
    public void excelReadTestOfEmptyLine() {
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("priceIndex.xls"));
        List<Map<String, Object>> readAll = reader.readAll();

        Assert.equals(4, readAll.size());
    }

    @Test
    public void excelReadTest() {
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
        List<List<Object>> readAll = reader.read();

        // 标题
        Assert.equals("姓名", readAll.get(0).get(0));
        Assert.equals("性别", readAll.get(0).get(1));
        Assert.equals("年龄", readAll.get(0).get(2));
        Assert.equals("鞋码", readAll.get(0).get(3));

        // 第一行
        Assert.equals("张三", readAll.get(1).get(0));
        Assert.equals("男", readAll.get(1).get(1));
        Assert.equals(11L, readAll.get(1).get(2));
        Assert.equals(41.5D, readAll.get(1).get(3));
    }

    @Test
    public void excelReadAsTextTest() {
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
        Assert.notNull(reader.readAsText(false));
    }

    @Test
    public void excel03ReadTest() {
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xls"));
        List<List<Object>> readAll = reader.read();

        // for (List<Object> list : readAll) {
        // Console.log(list);
        // }

        // 标题
        Assert.equals("姓名", readAll.get(0).get(0));
        Assert.equals("性别", readAll.get(0).get(1));
        Assert.equals("年龄", readAll.get(0).get(2));
        Assert.equals("分数", readAll.get(0).get(3));

        // 第一行
        Assert.equals("张三", readAll.get(1).get(0));
        Assert.equals("男", readAll.get(1).get(1));
        Assert.equals(11L, readAll.get(1).get(2));
        Assert.equals(33.2D, readAll.get(1).get(3));
    }

    @Test
    public void excel03ReadTest2() {
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xls"), "校园入学");
        List<List<Object>> readAll = reader.read();

        // 标题
        Assert.equals("班级", readAll.get(0).get(0));
        Assert.equals("年级", readAll.get(0).get(1));
        Assert.equals("学校", readAll.get(0).get(2));
        Assert.equals("入学时间", readAll.get(0).get(3));
        Assert.equals("更新时间", readAll.get(0).get(4));
    }

    @Test
    public void excelReadToMapListTest() {
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
        List<Map<String, Object>> readAll = reader.readAll();

        Assert.equals("张三", readAll.get(0).get("姓名"));
        Assert.equals("男", readAll.get(0).get("性别"));
        Assert.equals(11L, readAll.get(0).get("年龄"));
    }

    @Test
    public void excelReadToBeanListTest() {
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
        reader.addHeaderAlias("姓名", "name");
        reader.addHeaderAlias("年龄", "age");
        reader.addHeaderAlias("性别", "gender");
        reader.addHeaderAlias("鞋码", "shoeSize");

        List<Person> all = reader.readAll(Person.class);
        Assert.equals("张三", all.get(0).getName());
        Assert.equals("男", all.get(0).getGender());
        Assert.equals(Integer.valueOf(11), all.get(0).getAge());
        Assert.equals(new BigDecimal("41.5"), all.get(0).getShoeSize());
    }

    @Test
    public void excelReadToBeanListTest2() {
        ExcelReader reader = ExcelUtil.getReader("f:/test/toBean.xlsx");
        reader.addHeaderAlias("姓名", "name");
        reader.addHeaderAlias("年龄", "age");
        reader.addHeaderAlias("性别", "gender");

        List<Person> all = reader.read(0, 2, Person.class);
        for (Person person : all) {
            Console.log(person);
        }
    }

    @Data
    public static class Person {
        private String name;
        private String gender;
        private Integer age;
        private BigDecimal shoeSize;
    }

    @Test
    public void readDoubleTest() {
        ExcelReader reader = ExcelUtil.getReader("f:/test/doubleTest.xls");
        final List<List<Object>> read = reader.read();
        for (List<Object> list : read) {
            Console.log(list.get(8));
        }
    }

    @Test
    public void mergeReadTest() {
        final ExcelReader reader = ExcelUtil.getReader("merge_test.xlsx");
        final List<List<Object>> read = reader.read();
        // 验证合并单元格在两行中都可以取到值
        Assert.equals(11L, read.get(1).get(2));
        Assert.equals(11L, read.get(2).get(2));
    }

    @Test
    public void readCellsTest() {
        final ExcelReader reader = ExcelUtil.getReader("merge_test.xlsx");
        reader.read((cell, value) -> Console.log("{}, {} {}", cell.getRowIndex(), cell.getColumnIndex(), value));
    }

    @Test
    public void readTest() {
        // 测试合并单元格是否可以正常读到第一个单元格的值
        final ExcelReader reader = ExcelUtil.getReader("d:/test/人员体检信息表.xlsx");
        final List<List<Object>> read = reader.read();
        for (List<Object> list : read) {
            Console.log(list);
        }
    }

    @Test
    public void nullValueEditTest() {
        final ExcelReader reader = ExcelUtil.getReader("null_cell_test.xlsx");
        reader.setCellEditor((cell, value) -> ObjectUtil.defaultIfNull(value, "#"));
        final List<List<Object>> read = reader.read();

        // 对于任意一个单元格有值的情况下，之前的单元格值按照null处理
        Assert.equals(1, read.get(1).size());
        Assert.equals(2, read.get(2).size());
        Assert.equals(3, read.get(3).size());

        Assert.equals("#", read.get(2).get(0));
        Assert.equals("#", read.get(3).get(0));
        Assert.equals("#", read.get(3).get(1));
    }

    @Test
    public void readEmptyTest() {
        final ExcelReader reader = ExcelUtil.getReader("d:/test/issue.xlsx");
        final List<Map<String, Object>> maps = reader.readAll();
        Console.log(maps);
    }

    @Test
    public void readNullRowTest() {
        final ExcelReader reader = ExcelUtil.getReader("d:/test/1.-.xls");
        reader.read((CellHandler) Console::log);
    }

    @Test
    public void readColumnTest() {
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
        final List<Object> objects = reader.readColumn(0, 1);

        Assert.equals(3, objects.size());
        Assert.equals("张三", objects.get(0));
        Assert.equals("李四", objects.get(1));
        Assert.equals("", objects.get(2));
    }

    @Test
    public void readColumnNPETest() {
        // https://github.com/dromara/hutool/pull/2234
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("read_row_npe.xlsx"));
        reader.readColumn(0, 1);
    }
}
