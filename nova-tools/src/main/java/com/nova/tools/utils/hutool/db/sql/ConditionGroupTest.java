package com.nova.tools.utils.hutool.db.sql;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.ConditionBuilder;
import cn.hutool.db.sql.ConditionGroup;
import cn.hutool.db.sql.LogicalOperator;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class ConditionGroupTest {
    @Test
    public void ConditionGroupToStringTest() {
        Condition condition1 = new Condition("a", "A");
        Condition condition2 = new Condition("b", "B");
        condition2.setLinkOperator(LogicalOperator.OR);
        Condition condition3 = new Condition("c", "C");
        Condition condition4 = new Condition("d", "D");

        ConditionGroup cg = new ConditionGroup();
        cg.addConditions(condition1, condition2);

        // 条件组嵌套情况
        ConditionGroup cg2 = new ConditionGroup();
        cg2.addConditions(cg, condition3);

        final ConditionBuilder conditionBuilder = ConditionBuilder.of(cg2, condition4);

        Assert.equals("((a = ? OR b = ?) AND c = ?) AND d = ?", conditionBuilder.build());
        Assert.equals(ListUtil.of("A", "B", "C", "D"), conditionBuilder.getParamValues());
    }
}
