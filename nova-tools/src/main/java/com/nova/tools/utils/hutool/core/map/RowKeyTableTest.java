package com.nova.tools.utils.hutool.core.map;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.map.multi.Table;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class RowKeyTableTest {

    @Test
    public void putGetTest() {
        final Table<Integer, Integer, Integer> table = new RowKeyTable<>();
        table.put(1, 2, 3);
        table.put(1, 6, 4);

        Assert.equals(new Integer(3), table.get(1, 2));
        Assert.isNull(table.get(1, 3));

        //判断row和column确定的二维点是否存在
        Assert.isTrue(table.contains(1, 2));
        Assert.isFalse(table.contains(1, 3));

        //判断列
        Assert.isTrue(table.containsColumn(2));
        Assert.isFalse(table.containsColumn(3));

        // 判断行
        Assert.isTrue(table.containsRow(1));
        Assert.isFalse(table.containsRow(2));


        // 获取列
        Map<Integer, Integer> column = table.getColumn(6);
        Assert.equals(1, column.size());
        Assert.equals(new Integer(4), column.get(1));
    }
}
