package com.nova.tools.utils.hutool.core.comparator;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CompareUtilTest {

    @Test
    public void compareTest() {
        int compare = CompareUtil.compare(null, "a", true);
        Assert.isTrue(compare > 0);

        compare = CompareUtil.compare(null, "a", false);
        Assert.isTrue(compare < 0);
    }

    @Test
    public void comparingPinyin() {
        List<String> list = ListUtil.toList("成都", "北京", "上海", "深圳");

        List<String> ascendingOrderResult = ListUtil.of("北京", "成都", "上海", "深圳");
        List<String> descendingOrderResult = ListUtil.of("深圳", "上海", "成都", "北京");

        // 正序
        list.sort(CompareUtil.comparingPinyin(e -> e));
        Assert.equals(list, ascendingOrderResult);

        // 反序
        list.sort(CompareUtil.comparingPinyin(e -> e, true));
        Assert.equals(list, descendingOrderResult);
    }
}
