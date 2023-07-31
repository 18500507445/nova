package com.nova.book.design.create.prototype;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;


/**
 * @description: 原型模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        // 原型模式完成对象的创建
        // 先造一只羊
        Sheep sheep = new Sheep("zz", "blue");
        sheep.friend = new Sheep("qq", "white");

        // 克隆
        Sheep sheep2 = (Sheep) sheep.clone();
        Sheep sheep3 = (Sheep) sheep.clone();

        System.err.println(JSONUtil.toJsonStr(sheep));
        System.err.println(JSONUtil.toJsonStr(sheep2));
        System.err.println(JSONUtil.toJsonStr(sheep3));
    }

}