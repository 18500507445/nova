package com.nova.tools.utils.hutool.db.sql;

import cn.hutool.core.lang.Assert;
import cn.hutool.db.sql.Condition;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ConditionTest {

    @Test
    public void toStringTest() {
        Condition conditionNull = new Condition("user", null);
        Assert.equals("user IS NULL", conditionNull.toString());

        Condition conditionNotNull = new Condition("user", "!= null");
        Assert.equals("user IS NOT NULL", conditionNotNull.toString());

        Condition condition2 = new Condition("user", "= zhangsan");
        Assert.equals("user = ?", condition2.toString());

        Condition conditionLike = new Condition("user", "like %aaa");
        Assert.equals("user LIKE ?", conditionLike.toString());

        Condition conditionIn = new Condition("user", "in 1,2,3");
        Assert.equals("user IN (?,?,?)", conditionIn.toString());

        Condition conditionBetween = new Condition("user", "between 12 and 13");
        Assert.equals("user BETWEEN ? AND ?", conditionBetween.toString());
    }

    @Test
    public void toStringNoPlaceHolderTest() {
        Condition conditionNull = new Condition("user", null);
        conditionNull.setPlaceHolder(false);
        Assert.equals("user IS NULL", conditionNull.toString());

        Condition conditionNotNull = new Condition("user", "!= null");
        conditionNotNull.setPlaceHolder(false);
        Assert.equals("user IS NOT NULL", conditionNotNull.toString());

        Condition conditionEquals = new Condition("user", "= zhangsan");
        conditionEquals.setPlaceHolder(false);
        Assert.equals("user = zhangsan", conditionEquals.toString());

        Condition conditionLike = new Condition("user", "like %aaa");
        conditionLike.setPlaceHolder(false);
        Assert.equals("user LIKE '%aaa'", conditionLike.toString());

        Condition conditionIn = new Condition("user", "in 1,2,3");
        conditionIn.setPlaceHolder(false);
        Assert.equals("user IN (1,2,3)", conditionIn.toString());

        Condition conditionBetween = new Condition("user", "between 12 and 13");
        conditionBetween.setPlaceHolder(false);
        Assert.equals("user BETWEEN 12 AND 13", conditionBetween.toString());
    }

    @Test
    public void parseTest() {
        final Condition age = Condition.parse("age", "< 10");
        Assert.equals("age < ?", age.toString());
        // issue I38LTM
        Assert.equals(BigDecimal.class, age.getValue().getClass());
    }

    @Test
    public void parseInTest() {
        final Condition age = Condition.parse("age", "in 1,2,3");
        Assert.equals("age IN (?,?,?)", age.toString());
    }
}
