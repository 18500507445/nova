package com.nova.tools.utils.hutool.core.math;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.math.Combination;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 组合单元测试
 *
 * @author looly
 */
public class CombinationTest {

    @Test
    public void countTest() {
        long result = Combination.count(5, 2);
        Assert.equals(10, result);

        result = Combination.count(5, 5);
        Assert.equals(1, result);

        result = Combination.count(5, 0);
        Assert.equals(1, result);

        long resultAll = Combination.countAll(5);
        Assert.equals(31, resultAll);
    }

    @Test
    public void selectTest() {
        Combination combination = new Combination(new String[]{"1", "2", "3", "4", "5"});
        List<String[]> list = combination.select(2);
        Assert.equals(Combination.count(5, 2), list.size());

        Assert.equals(new String[]{"1", "2"}, list.get(0));
        Assert.equals(new String[]{"1", "3"}, list.get(1));
        Assert.equals(new String[]{"1", "4"}, list.get(2));
        Assert.equals(new String[]{"1", "5"}, list.get(3));
        Assert.equals(new String[]{"2", "3"}, list.get(4));
        Assert.equals(new String[]{"2", "4"}, list.get(5));
        Assert.equals(new String[]{"2", "5"}, list.get(6));
        Assert.equals(new String[]{"3", "4"}, list.get(7));
        Assert.equals(new String[]{"3", "5"}, list.get(8));
        Assert.equals(new String[]{"4", "5"}, list.get(9));

        List<String[]> selectAll = combination.selectAll();
        Assert.equals(Combination.countAll(5), selectAll.size());

        List<String[]> list2 = combination.select(0);
        Assert.equals(1, list2.size());
    }
}
