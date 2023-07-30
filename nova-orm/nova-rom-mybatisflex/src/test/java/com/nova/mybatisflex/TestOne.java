package com.nova.mybatisflex;

import cn.hutool.json.JSONUtil;
import com.nova.mybatisflex.entity.UserDO;
import com.nova.mybatisflex.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: wzh
 * @description 测试类
 * @date: 2023/07/30 14:09
 */
@SpringBootTest
public class TestOne {
    
    @Resource
    private UserMapper userMapper;
    
    @Test
    public void queryAll() {
        List<UserDO> list = userMapper.selectAll();
        System.out.println("jsonStr = " + JSONUtil.toJsonStr(list));
    }
    
}
