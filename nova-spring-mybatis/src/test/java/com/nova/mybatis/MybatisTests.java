package com.nova.mybatis;

import com.nova.mybatis.config.MybatisConfiguration;
import com.nova.mybatis.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

/**
 * @description: 引入spring-test依赖 使用@ExtendWith、@ContextConfiguration获取上下文
 * @author: wangzehui
 * @date: 2022/12/31 20:34
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MybatisConfiguration.class)
public class MybatisTests {

    @Resource
    public StudentMapper studentMapper;

    /**
     * xml方式
     */
    @Test
    public void testXml() {
        System.out.println(studentMapper.getStudent());
    }

    /**
     * 直接配置数据源
     */
    @Test
    public void testConfig() {
        System.out.println(studentMapper.getStudent());
    }
}
