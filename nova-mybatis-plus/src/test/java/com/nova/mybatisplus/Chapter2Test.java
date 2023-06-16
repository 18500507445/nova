package com.nova.mybatisplus;

import com.nova.mybatisplus.chapter2.TwoUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description 第二章测试类：基础篇，简单的增删改查
 * @date: 2023/06/16 09:35
 */
@SpringBootTest
public class Chapter2Test {

    @Resource
    private TwoUserMapper userMapper;

    @Test
    public void demoA() {

    }


}
