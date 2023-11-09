package com.nova.orm.mybatis.service.impl;

import com.nova.orm.mybatis.mapper.StudentMapper;
import com.nova.orm.mybatis.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/1 16:18
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    /**
     * 事务的隔离级别默认：PROPAGATION_REQUIRED
     * 也就是说insertTwo和insertOne都有事物，one里调用two，那么事物2会加入到事物1里
     * 执行结果都不插入
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertTwo() {
        studentMapper.insertTwo();
        throw new RuntimeException("我是insertTwo异常！");
    }

    /**
     * 事务的隔离级别：SUPPORTS（不需要上下文）
     * 单独调用insertThree方法，并不会以事务的方式执行
     * 当发生异常时，虽然依然存在AOP增强，但是不会进行回滚操作
     * 而现在再调用testSupports方法，才会以事务的方式执行。
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public void insertThree() {
        studentMapper.insertThree();
        throw new RuntimeException("我是insertThree异常！");
    }

    /**
     * 事务的隔离级别：MANDATORY 如果当前方法并没有在任何事务中进行，会直接出现异常
     * 解释：直接运行insertFour抛出异常，因为他不在其他事务中
     * No existing transaction found for transaction marked with propagation 'mandatory'
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    @Override
    public void insertFour() {
        studentMapper.insertFour();
        //throw new RuntimeException("我是insertFour异常！");
    }

    /**
     * 事务的隔离级别：NESTED
     * 如果存在外层事务，则此方法单独创建一个子事务，回滚只会影响到此子事务，需要catch住，否则影响外层事物也跟着回滚
     * <p>
     * 实际上就是利用创建Savepoint，然后回滚到此保存点实现的。NEVER级别表示此方法不应该加入到任何事务中，
     * <p>
     * 无论是Propagation.REQUIRES_NEW，还是Propagation.NESTED，只要是内部事务抛出异常数据库都会回滚
     * <p>
     * 区别在于Propagation.REQUIRES_NEW是一个全新开启的事务，即使外部事务抛出异常发生数据库回滚，
     * 也不影响内部事务的提交。而Propagation.NESTED则更像是一个事务的子事务，受外部事务的影响。
     * <p>
     * 什么时候用到Propagation.REQUIRES_NEW呢？举个例子，像是在生成订单的业务里，往往生成订单号的逻辑会单独分离出来，
     * 这个时候就可以给这个生成订单号的函数声明propagation=Propagation.REQUIRES_NEW，
     * 因为这样即使生成订单的过程出现错误也没必要让生成订单号相关的数据库回滚，生成就生成了，数据库回滚是浪费资源的。
     */
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    @Override
    public void insertFive() {
        studentMapper.insertFive();
        throw new RuntimeException("我是insertFive异常！");
    }

    /**
     * 事务的隔离级别：NEVER
     * 表示此方法不应该加入到任何事务中，其余类型适用于同时操作多数据源情况下的分布式事务管理
     */
    @Transactional(propagation = Propagation.NEVER, rollbackFor = Exception.class)
    @Override
    public void insertSix() {
        studentMapper.insertSix();
        //throw new RuntimeException("我是insertSix异常！");
    }

    /**
     * 内外事务相互独立，互不影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void insertSeven() {
        studentMapper.insertSeven();
        //throw new RuntimeException("我是insertSeven异常！");
    }
}
