package com.nova.tools.utils.hutool.db.sql;

import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Order;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class SqlBuilderTest {

	@Test
	public void queryNullTest() {
		SqlBuilder builder = SqlBuilder.create().select().from("user").where(new Condition("name", "= null"));
		Assert.equals("SELECT * FROM user WHERE name IS NULL", builder.build());

		SqlBuilder builder2 = SqlBuilder.create().select().from("user").where(new Condition("name", "is null"));
		Assert.equals("SELECT * FROM user WHERE name IS NULL", builder2.build());

		SqlBuilder builder3 = SqlBuilder.create().select().from("user").where(new Condition("name", "!= null"));
		Assert.equals("SELECT * FROM user WHERE name IS NOT NULL", builder3.build());

		SqlBuilder builder4 = SqlBuilder.create().select().from("user").where(new Condition("name", "is not null"));
		Assert.equals("SELECT * FROM user WHERE name IS NOT NULL", builder4.build());
	}

	@Test
	public void orderByTest() {
		SqlBuilder builder = SqlBuilder.create().select("id", "username").from("user")
				.join("role", SqlBuilder.Join.INNER)
				.on("user.id = role.user_id")
				.where(new Condition("age", ">=", 18),
						new Condition("username", "abc", Condition.LikeType.Contains)
				).orderBy(new Order("id"));

		Assert.equals("SELECT id,username FROM user INNER JOIN role ON user.id = role.user_id WHERE age >= ? AND username LIKE ? ORDER BY id", builder.build());
	}

	@Test
	public void likeTest() {
		Condition conditionEquals = new Condition("user", "123", Condition.LikeType.Contains);
		conditionEquals.setPlaceHolder(false);

		SqlBuilder sqlBuilder = new SqlBuilder();
		sqlBuilder.select("id");
		sqlBuilder.from("user");
		sqlBuilder.where(conditionEquals);
		String s1 = sqlBuilder.build();
		Assert.equals("SELECT id FROM user WHERE user LIKE '%123%'", s1);
	}
}