package com.nova.tools.utils.hutool.db;

import cn.hutool.db.Page;
import cn.hutool.db.sql.Order;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class PageTest {

	@Test
	public void addOrderTest() {
		Page page = new Page();
		page.addOrder(new Order("aaa"));
		Assert.equals(page.getOrders().length, 1);
		page.addOrder(new Order("aaa"));
		Assert.equals(page.getOrders().length, 2);
	}
}
