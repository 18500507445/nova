package com.nova.tools.utils.hutool.core.bean;

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.lang.Assert;
import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 * {@link DynaBean}单元测试
 *
 * @author: Looly
 */
public class DynaBeanTest {

    @Test
    public void beanTest() {
        User user = new User();
        DynaBean bean = DynaBean.create(user);
        bean.set("name", "李华");
        bean.set("age", 12);

        String name = bean.get("name");
        Assert.equals(user.getName(), name);
        int age = bean.get("age");
        Assert.equals(user.getAge(), age);

        //重复包装测试
        DynaBean bean2 = new DynaBean(bean);
        User user2 = bean2.getBean();
        Assert.equals(user, user2);

        //执行指定方法
        Object invoke = bean2.invoke("testMethod");
        Assert.equals("test for 李华", invoke);
    }


    @Test
    public void beanByStaticClazzConstructorTest() {
        String name_before = "李华";
        int age_before = 12;
        DynaBean bean = DynaBean.create(User.class);
        bean.set("name", name_before);
        bean.set("age", age_before);

        String name_after = bean.get("name");
        Assert.equals(name_before, name_after);
        int age_after = bean.get("age");
        Assert.equals(age_before, age_after);

        //重复包装测试
        DynaBean bean2 = new DynaBean(bean);
        User user2 = bean2.getBean();
        User user1 = bean.getBean();
        Assert.equals(user1, user2);

        //执行指定方法
        Object invoke = bean2.invoke("testMethod");
        Assert.equals("test for 李华", invoke);
    }


    @Test
    public void beanByInstanceClazzConstructorTest() {
        String name_before = "李华";
        int age_before = 12;
        DynaBean bean = new DynaBean(User.class);
        bean.set("name", name_before);
        bean.set("age", age_before);

        String name_after = bean.get("name");
        Assert.equals(name_before, name_after);
        int age_after = bean.get("age");
        Assert.equals(age_before, age_after);

        //重复包装测试
        DynaBean bean2 = new DynaBean(bean);
        User user2 = bean2.getBean();
        User user1 = bean.getBean();
        Assert.equals(user1, user2);

        //执行指定方法
        Object invoke = bean2.invoke("testMethod");
        Assert.equals("test for 李华", invoke);
    }

    @Data
    public static class User {
        private String name;
        private int age;

        public String testMethod() {
            return "test for " + this.name;
        }
    }
}
