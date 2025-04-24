package com.nova.tools.utils.hutool.core.math;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.math.Arrangement;
import cn.hutool.core.math.MathUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 排列单元测试
 *
 * @author: looly
 */
public class ArrangementTest {

    @Test
    public void arrangementTest() {
        long result = Arrangement.count(4, 2);
        Assert.equals(12, result);

        result = Arrangement.count(4, 1);
        Assert.equals(4, result);

        result = Arrangement.count(4, 0);
        Assert.equals(1, result);

        long resultAll = Arrangement.countAll(4);
        Assert.equals(64, resultAll);
    }

    @Test
    public void selectTest() {
        Arrangement arrangement = new Arrangement(new String[]{"1", "2", "3", "4"});
        List<String[]> list = arrangement.select(2);
        Assert.equals(Arrangement.count(4, 2), list.size());
        Assert.equals(new String[]{"1", "2"}, list.get(0));
        Assert.equals(new String[]{"1", "3"}, list.get(1));
        Assert.equals(new String[]{"1", "4"}, list.get(2));
        Assert.equals(new String[]{"2", "1"}, list.get(3));
        Assert.equals(new String[]{"2", "3"}, list.get(4));
        Assert.equals(new String[]{"2", "4"}, list.get(5));
        Assert.equals(new String[]{"3", "1"}, list.get(6));
        Assert.equals(new String[]{"3", "2"}, list.get(7));
        Assert.equals(new String[]{"3", "4"}, list.get(8));
        Assert.equals(new String[]{"4", "1"}, list.get(9));
        Assert.equals(new String[]{"4", "2"}, list.get(10));
        Assert.equals(new String[]{"4", "3"}, list.get(11));

        List<String[]> selectAll = arrangement.selectAll();
        Assert.equals(Arrangement.countAll(4), selectAll.size());

        List<String[]> list2 = arrangement.select(0);
        Assert.equals(1, list2.size());
    }

    @Test
    public void selectTest2() {
        List<String[]> list = MathUtil.arrangementSelect(new String[]{"1", "1", "3", "4"});
        for (String[] strings : list) {
            Console.log(strings);
        }
    }
}
