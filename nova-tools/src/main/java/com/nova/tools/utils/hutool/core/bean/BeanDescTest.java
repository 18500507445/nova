package com.nova.tools.utils.hutool.core.bean;

import cn.hutool.core.bean.BeanDesc;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.PropDesc;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * {@link BeanDesc} 单元测试类
 *
 * @author: looly
 */
public class BeanDescTest {

    @Test
    public void propDescTes() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);
        Assert.equals("User", desc.getSimpleName());

        Assert.equals("age", desc.getField("age").getName());
        Assert.equals("getAge", desc.getGetter("age").getName());
        Assert.equals("setAge", desc.getSetter("age").getName());
        Assert.equals(1, desc.getSetter("age").getParameterTypes().length);
        Assert.equals(int.class, desc.getSetter("age").getParameterTypes()[0]);

    }

    @Test
    public void propDescTes2() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);

        PropDesc prop = desc.getProp("name");
        Assert.equals("name", prop.getFieldName());
        Assert.equals("getName", prop.getGetter().getName());
        Assert.equals("setName", prop.getSetter().getName());
        Assert.equals(1, prop.getSetter().getParameterTypes().length);
        Assert.equals(String.class, prop.getSetter().getParameterTypes()[0]);
    }

    @Test
    public void propDescOfBooleanTest() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);

        Assert.equals("isAdmin", desc.getGetter("isAdmin").getName());
        Assert.equals("setAdmin", desc.getSetter("isAdmin").getName());
        Assert.equals("isGender", desc.getGetter("gender").getName());
        Assert.equals("setGender", desc.getSetter("gender").getName());
    }

    @Test
    public void propDescOfBooleanTest2() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);

        Assert.equals("isIsSuper", desc.getGetter("isSuper").getName());
        Assert.equals("setIsSuper", desc.getSetter("isSuper").getName());
    }

    @Test
    public void getSetTest() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);

        User user = new User();
        desc.getProp("name").setValue(user, "张三");
        Assert.equals("张三", user.getName());

        Object value = desc.getProp("name").getValue(user);
        Assert.equals("张三", value);
    }

    public static class User {
        private String name;
        private int age;
        private boolean isAdmin;
        private boolean isSuper;
        private boolean gender;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public User setAge(int age) {
            this.age = age;
            return this;
        }

        public String testMethod() {
            return "test for " + this.name;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public boolean isIsSuper() {
            return isSuper;
        }

        public void setIsSuper(boolean isSuper) {
            this.isSuper = isSuper;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        @Override
        public String toString() {
            return "User [name=" + name + ", age=" + age + ", isAdmin=" + isAdmin + ", gender=" + gender + "]";
        }
    }
}
