package com.nova.orm.mybatis.service.impl;

import com.nova.orm.mybatis.mapper.StudentMapper;
import com.nova.orm.mybatis.service.StudentService;
import com.nova.orm.mybatis.service.TransactionService;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

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
            System.err.println(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testNever() {
        studentMapper.insertOne();
        studentService.insertSix();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void testNew() {
        studentMapper.insertOne();
        try {
            studentService.insertSeven();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Override
    @Transactional
    public void a() {
        studentMapper.insertA();
        //主线程等待，可以添加join()
        CompletableFuture.runAsync(() -> {
            b();
            c();
        }).join();
    }

    @Override
    @Transactional
    public void a1() {
        try {
            studentMapper.insertA();
            //主线程等待，可以添加join()
            CompletableFuture.runAsync(() -> {
                //（1）先获取当前线程的事务状态
                TransactionStatus subStatus = TransactionAspectSupport.currentTransactionStatus();
                try {
                    b();
                    c();
                } catch (Exception e) {
                    System.err.println("线程异常");
                    //（2）不要写成TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()这样，要先获取才生效
                    subStatus.setRollbackOnly();
                    throw new RuntimeException(e);
                }
            }).join();
        } catch (Exception e) {
            System.err.println("主线程异常");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

    }

    @Override
    public void aPro() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            studentMapper.insertA();
            CompletableFuture.runAsync(() -> {
                TransactionStatus status1 = transactionManager.getTransaction(new DefaultTransactionDefinition());
                try {
                    b();
                    c();
                    transactionManager.commit(status1);
                } catch (Exception e) {
                    System.err.println("线程异常");
                    transactionManager.rollback(status1);
                    throw new RuntimeException(e);
                }
            }).join();
            transactionManager.commit(status);
        } catch (Exception e) {
            System.err.println("主线程异常");
            transactionManager.rollback(status);
        }
    }

    public void b() {
        studentMapper.insertB();
    }

    public int c() {
        studentMapper.insertC();
        return 1 / 0;
    }
}
