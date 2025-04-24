package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.EnumUtil;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * {@link EnumUtil} 枚举单元测试
 *
 * @author:looly
 */
public class EnumUtilTest {

    @Test
    public void getNamesTest() {
        List<String> names = EnumUtil.getNames(TestEnum.class);
        Assert.equals(CollUtil.newArrayList("TEST1", "TEST2", "TEST3"), names);
    }

    @Test
    public void getFieldValuesTest() {
        List<Object> types = EnumUtil.getFieldValues(TestEnum.class, "type");
        Assert.equals(CollUtil.newArrayList("type1", "type2", "type3"), types);
    }

    @Test
    public void getFieldNamesTest() {
        List<String> names = EnumUtil.getFieldNames(TestEnum.class);
        Assert.isTrue(names.contains("type"));
        Assert.isTrue(names.contains("name"));
    }

    @Test
    public void getByTest() {
        // 枚举中字段互相映射使用
        TestEnum testEnum = EnumUtil.getBy(TestEnum::ordinal, 1);
        Assert.equals("TEST2", testEnum.name());

        TestEnum byType = EnumUtil.getBy(TestEnum::getType, 1, TestEnum.TEST1);
        System.err.println("byType = " + byType);
    }

    @Test
    public void getFieldByTest() {
        // 枚举中字段互相映射使用
        String type = EnumUtil.getFieldBy(TestEnum::getType, Enum::ordinal, 1);
        Assert.equals("type2", type);

        int ordinal = EnumUtil.getFieldBy(TestEnum::ordinal, Enum::ordinal, 1);
        Assert.equals(1, ordinal);
    }

    @Test
    public void likeValueOfTest() {
        TestEnum value = EnumUtil.likeValueOf(TestEnum.class, "type2");
        Assert.equals(TestEnum.TEST2, value);
    }

    @Test
    public void getEnumMapTest() {
        Map<String, TestEnum> enumMap = EnumUtil.getEnumMap(TestEnum.class);
        Assert.equals(TestEnum.TEST1, enumMap.get("TEST1"));
    }

    @Test
    public void getNameFieldMapTest() {
        Map<String, Object> enumMap = EnumUtil.getNameFieldMap(TestEnum.class, "type");
        assert enumMap != null;
        Assert.equals("type1", enumMap.get("TEST1"));
    }

    @Getter
    public enum TestEnum {
        TEST1("type1"), TEST2("type2"), TEST3("type3");

        TestEnum(String type) {
            this.type = type;
        }

        private final String type;
        @SuppressWarnings("unused")
        private String name;

    }
}
