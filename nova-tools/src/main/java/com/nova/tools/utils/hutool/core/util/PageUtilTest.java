package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.PageUtil;
import org.junit.jupiter.api.Test;


/**
 * {@link PageUtil} 分页单元测试
 *
 * @author
 */
public class PageUtilTest {

    @Test
    public void transToStartEndTest() {
        int[] startEnd1 = PageUtil.transToStartEnd(0, 10);
        Assert.equals(0, startEnd1[0]);
        Assert.equals(10, startEnd1[1]);

        int[] startEnd2 = PageUtil.transToStartEnd(1, 10);
        Assert.equals(10, startEnd2[0]);
        Assert.equals(20, startEnd2[1]);
    }

    @Test
    public void totalPage() {
        int totalPage = PageUtil.totalPage(20, 3);
        Assert.equals(7, totalPage);
    }

    @Test
    public void rainbowTest() {
        int[] rainbow = PageUtil.rainbow(5, 20, 6);
        Assert.equals(new int[]{3, 4, 5, 6, 7, 8}, rainbow);
    }
}
