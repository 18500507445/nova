package com.nova.tools.utils.hutool.core.bean.copier;

import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class BeanCopierTest {

    @Test
    public void beanToMapIgnoreNullTest() {
        final A a = new A();

        HashMap<Object, Object> map = BeanCopier.create(a, new HashMap<>(), CopyOptions.create()).copy();
        Assert.equals(1, map.size());
        Assert.isTrue(map.containsKey("value"));
        Assert.isNull(map.get("value"));

        // 忽略null的情况下，空字段不写入map
        map = BeanCopier.create(a, new HashMap<>(), CopyOptions.create().ignoreNullValue()).copy();
        Assert.isFalse(map.containsKey("value"));
        Assert.equals(0, map.size());
    }

    /**
     * 测试在非覆盖模式下，目标对象有值则不覆盖
     */
    @Test
    public void beanToBeanNotOverrideTest() {
        final A a = new A();
        a.setValue("123");
        final B b = new B();
        b.setValue("abc");

        final BeanCopier<B> copier = BeanCopier.create(a, b, CopyOptions.create().setOverride(false));
        copier.copy();

        Assert.equals("abc", b.getValue());
    }

    /**
     * 测试在覆盖模式下，目标对象值被覆盖
     */
    @Test
    public void beanToBeanOverrideTest() {
        final A a = new A();
        a.setValue("123");
        final B b = new B();
        b.setValue("abc");

        final BeanCopier<B> copier = BeanCopier.create(a, b, CopyOptions.create());
        copier.copy();

        Assert.equals("123", b.getValue());
    }

    /**
     * 为{@code null}则写，否则忽略。如果覆盖，则不判断直接写
     */
    @Test
    public void issues2484Test() {
        final A a = new A();
        a.setValue("abc");
        final B b = new B();
        b.setValue("123");

        BeanCopier<B> copier = BeanCopier.create(a, b, CopyOptions.create().setOverride(false));
        copier.copy();
        Assert.equals("123", b.getValue());

        b.setValue(null);
        copier = BeanCopier.create(a, b, CopyOptions.create().setOverride(false));
        copier.copy();
        Assert.equals("abc", b.getValue());
    }

    @Data
    private static class A {
        private String value;
    }

    @Data
    private static class B {
        private String value;
    }
}
