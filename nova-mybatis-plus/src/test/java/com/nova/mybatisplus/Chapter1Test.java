package com.nova.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.nova.mybatisplus.chapter1.OneUserMapper;
import com.nova.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: wzh
 * @description 第一章测试类：入门篇
 * @date: 2023/06/15 20:01
 */
@SpringBootTest
public class Chapter1Test {

    @Resource
    private OneUserMapper oneUserMapper;

    @Test
    public void selectList() {
        List<UserDO> userDOList = oneUserMapper.selectList(null);
        String jsonStr = JSONUtil.toJsonStr(userDOList);
        System.out.println("jsonStr = " + jsonStr);
    }

}
