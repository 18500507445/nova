package com.nova.tools.utils.hutool.db.sql;

import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.ConditionBuilder;
import cn.hutool.db.sql.LogicalOperator;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class ConditionBuilderTest {

    @Test
    public void buildTest() {
        Condition c1 = new Condition("user", null);
        Condition c2 = new Condition("name", "!= null");
        c2.setLinkOperator(LogicalOperator.OR);
        Condition c3 = new Condition("group", "like %aaa");

        final ConditionBuilder builder = ConditionBuilder.of(c1, c2, c3);
        final String sql = builder.build();
        Assert.equals("user IS NULL OR name IS NOT NULL AND group LIKE ?", sql);
        Assert.equals(1, builder.getParamValues().size());
        Assert.equals("%aaa", builder.getParamValues().get(0));
    }
}
