package com.nova.mybatisplus;

import com.nova.mybatisplus.chapter4.FourUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description 第四章测试类：进阶篇
 * @date: 2023/06/15 20:01
 */
@SpringBootTest
public class Chapter4Test {

    @Resource
    private FourUserMapper fourUserMapper;

    @Test
    public void demoA() {

    }


}
