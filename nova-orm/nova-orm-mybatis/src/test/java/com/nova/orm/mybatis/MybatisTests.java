package com.nova.orm.mybatis;


import com.nova.orm.mybatis.config.MybatisConfiguration;
import com.nova.orm.mybatis.mapper.StudentMapper;
import com.nova.orm.mybatis.service.StudentService;
import com.nova.orm.mybatis.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

/**
 * @description: 引入spring-test依赖 使用@ExtendWith、@ContextConfiguration获取上下文
 * @author: wzh
 * @date: 2022/12/31 20:34
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MybatisConfiguration.class)
public class MybatisTests {

    @Resource
    public StudentMapper studentMapper;

    @Resource
    public StudentService studentService;

    @Resource
    public TransactionService transactionService;

    /**
     * xml方式或者配置数据源
     */
    @Test
    public void testConfig() {
        System.err.println(studentMapper.getStudent());
    }

    /**
     * 测试事务
     */
    @Test
    public void testTransactional() {
        transactionService.testTransactional();
    }

    /**
     * 默认
     */
    @Test
    public void testDefault() {
        transactionService.testDefault();
    }

    /**
     * Supports
     */
    @Test
    public void testSupports() {
        //transactionService.testSupports();
        studentService.insertThree();
    }

    /**
     * Mandatory
     */
    @Test
    public void testMandatory() {
        transactionService.testMandatory();
        //studentService.insertFour();
    }

    /**
     * Nested
     */
    @Test
    public void testNested() {
        transactionService.testNested();
    }

    /**
     * Never
     */
    @Test
    public void testNever() {
        //transactionService.testNever();
        studentService.insertSix();
    }

    /**
     * New
     */
    @Test
    public void testNew() {
        transactionService.testNew();
    }

    /**
     * 不加join（主线程不等待），a入库，bc回滚
     * 加join（主线程等待），a回滚，bc入库
     */
    @Test
    public void testA() {
        transactionService.a();
    }

    /**
     * 普通版，已经符合要求
     */
    @Test
    public void testA1() {
        transactionService.a1();
    }

    /**
     * 升级版，纯手动管理事务，abc都回滚
     */
    @Test
    public void testPro() {
        transactionService.aPro();
    }
}
