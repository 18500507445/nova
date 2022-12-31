package com.nova.mybatis;

import com.nova.mybatis.config.MybatisConfiguration;
import com.nova.mybatis.mapper.StudentMapper;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/12/31 20:34
 */
public class MybatisTests {

    /**
     * xml方式
     */
    @Test
    public void testXml() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MybatisConfiguration.class);
        SqlSessionTemplate template = context.getBean(SqlSessionTemplate.class);
        StudentMapper studentMapper = template.getMapper(StudentMapper.class);
        System.out.println(studentMapper.getStudent());
    }

    /**
     * 直接配置数据源
     */
    @Test
    public void testConfig(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MybatisConfiguration.class);
        StudentMapper studentMapper = context.getBean(StudentMapper.class);
        System.out.println(studentMapper.getStudent());
    }
}
