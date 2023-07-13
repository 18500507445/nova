package com.nova.myabtis.service.impl;

import com.nova.myabtis.mapper.StudentMapper;
import com.nova.myabtis.service.StudentService;
import com.nova.myabtis.service.TransactionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/1 21:54
 */
@Component
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private StudentService studentService;

    /**
     * Transactional就相当于
     * DataSourceTransactionManager manager = new DataSourceTransactionManager();
     * try manager.commit()
     * catch manager.rollback()
     * <p>
     * 抛出异常 一个数据都进不到数据库，因为事务要么都完成，要么都不完成
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testTransactional() {
        studentMapper.insertOne();
        if (true) {
            throw new RuntimeException("我是insertTransactional异常！");
        }
        studentMapper.insertOne();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testDefault() {
        studentMapper.insertOne();
        studentService.insertTwo();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testSupports() {
        studentMapper.insertOne();
        studentService.insertThree();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testMandatory() {
        studentMapper.insertOne();
        studentService.insertFour();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testNested() {
        studentMapper.insertOne();
        try {
            studentService.insertFive();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testNever() {
        studentMapper.insertOne();
        studentService.insertSix();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void testNew() {
        studentMapper.insertOne();
        try {
            studentService.insertSeven();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
