package com.nova.tools.java8.optional;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author wzh
 * @date 2018/2/11
 */
@Slf4j
class OptionalDemo {

    /**
     * Java8之前，过多的退出语句
     */
    public void saveUser(User user) {
        if (null == user) {
            return;
        }
        if (null == user.getAddress()) {
            return;
        }
    }

    /**
     * 1. 创建 Optional
     */
    @Test
    public void createOptional() {
        // 创建空的Optional
        Optional<Address> optionalAddress = Optional.empty();

        // null值会有空指针 npe
        Optional<Address> optionalAddress2 = Optional.of(new Address());

        // 可接受null的Optional
        Optional<Address> optionalAddress3 = Optional.ofNullable(new Address());
    }

    /**
     * 2. 使用 map 从 Optional 对象中提取和转换值
     */
    @Test
    public void map() {
        User user = new User();
        user.setUsername("wzh");
        user.setPassword("123456");
        user.setAddress(new Address("达尔文路", "88号"));
        user.setAge(30);
        Optional<User> optionalUser = Optional.of(user);

        //orElse调用方法不管street是否为null都进入方法，orElseGet就不会进去，需要做好区分，这是一个坑
        String s1 = optionalUser.map(User::getAddress).map(Address::getStreet).orElse(printf());
        String s2 = optionalUser.map(User::getAddress).map(Address::getStreet).orElseGet(this::printf);
        System.err.println("s1 = " + s1);
        System.err.println("s2 = " + s2);
    }

    private String printf() {
        System.err.println("操作数据库");
        return "打印";
    }


    @Test
    public void assertTest() {
        try {
            User user = new User();
            Assert.notNull(user, "用户不能为空");
            System.err.println("继续做");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
