package com.nova.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.nova.mybatisplus.chapter1.UserMapper;
import com.nova.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: wzh
 * @description 测试类
 * @date: 2023/06/15 20:01
 */
@SpringBootTest
public class MybatisPlusTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void selectList() {
        List<UserDO> userDOList = userMapper.selectList(null);
        String jsonStr = JSONUtil.toJsonStr(userDOList);
        System.out.println("jsonStr = " + jsonStr);
    }

}
