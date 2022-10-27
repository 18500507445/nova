package com.nova.tools.utils.hutool.db.ds;

import cn.hutool.db.ds.DataSourceWrapper;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class DataSourceWrapperTest {

    @Test
    public void cloneTest() {
        final SimpleDataSource simpleDataSource = new SimpleDataSource("jdbc:sqlite:test.db", "", "");
        final DataSourceWrapper wrapper = new DataSourceWrapper(simpleDataSource, "test.driver");

        final DataSourceWrapper clone = wrapper.clone();
        Assert.equals("test.driver", clone.getDriver());
        Assert.equals(simpleDataSource, clone.getRaw());
    }
}
