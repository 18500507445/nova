package com.nova.tools.utils.hutool.db;

import cn.hutool.core.lang.Assert;
import cn.hutool.db.Page;
import cn.hutool.db.PageResult;
import cn.hutool.db.sql.Order;
import org.junit.jupiter.api.Test;

/**
 * @description: hutool的分页实体类、排序实体类
 */
public class PageResultTest {

    @Test
    public void isLastTest() {
        // 每页2条，共10条，总共5页，第一页是0，最后一页应该是4
        final PageResult<String> result = new PageResult<>(4, 2, 10);
        Assert.isTrue(result.isLast());
    }

    @Test
    public void addOrderTest() {
        Page page = new Page();
        page.addOrder(new Order("aaa"));
        Assert.equals(page.getOrders().length, 1);
        page.addOrder(new Order("aaa"));
        Assert.equals(page.getOrders().length, 2);
    }
}
