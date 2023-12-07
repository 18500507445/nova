package com.nova.orm.mybatis.service.impl;

import com.nova.orm.mybatis.mapper.StudentMapper;
import com.nova.orm.mybatis.service.StudentService;
import com.nova.orm.mybatis.service.TransactionService;
import lombok.SneakyThrows;
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

    /**
     *
     * 异步任务和外层事务没有任何关系，加不加join，bc只要能运行到，都会入库，不会回滚
     * （1）加join，会阻塞主线程，那么a会进行回滚，是因为主线程可以感知异常抛出（我测试了，确实能catch到）,bc和外层事务没关系所以入库了
     * （2）不加join，不会阻塞主线程，a正常入库，是因为主线程感知不到异常。因为没执行到异步任务，主线程就结束了（我测试了，b只是进入，b并没有完成），所以不存在b和c回滚的情况
     * （3）可以主线程进行sleep测试，这样abc都会入库
     */
    @Override
    @Transactional
    @SneakyThrows
    public void a() {
        System.err.println("a开始执行");
        studentMapper.insertA();
        CompletableFuture.runAsync(() -> {
            b();
            c();
        }).join();
        System.err.println("a完成执行");
    }

    public void b() {
        System.err.println("b开始执行");
        studentMapper.insertB();
        System.err.println("b完成执行");
    }

    public int c() {
        System.err.println("c开始执行");
        studentMapper.insertC();
        System.err.println("c完成执行");
        return 1 / 0;
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


}
