package com.nova.tools.utils.hutool.core.date;

import cn.hutool.core.date.DateField;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class DateFieldTest {

    @Test
    public void ofTest() {
        DateField field = DateField.of(11);
        Assert.equals(DateField.HOUR_OF_DAY, field);
        field = DateField.of(12);
        Assert.equals(DateField.MINUTE, field);
    }
}
