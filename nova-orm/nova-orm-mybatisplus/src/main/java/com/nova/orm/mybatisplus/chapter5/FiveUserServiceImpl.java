package com.nova.orm.mybatisplus.chapter5;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nova.orm.mybatisplus.entity.MyOrder;
import com.nova.orm.mybatisplus.entity.UserFiveDO;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.math.BigDecimal;

/**
 * @author: wzh
 * @description userServiceImpl
 * @date: 2023/06/15 19:54
 */
@Service
@AllArgsConstructor
public class FiveUserServiceImpl extends ServiceImpl<FiveUserMapper, UserFiveDO> implements FiveUserService {

    private final FiveUserMapper fiveUserMapper;

    private final MyOrderMapper myOrderMapper;

    private final DataSourceTransactionManager transactionManager;

    @Override
    public void defaultCase() {
        //手动开启事务
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            UserFiveDO userDO = new UserFiveDO();
            userDO.setName("transaction").setAge(1).setEmail("xxxxx@qq.com").setGender(GenderEnum.MAN);
            fiveUserMapper.insert(userDO);

            //模拟异常
//            System.out.println(1 / 0);

            // 提交事务
            transactionManager.commit(status);
        } catch (TransactionException e) {
            // 回滚事务
            transactionManager.rollback(status);

            //方法2：用于开启注解@Transactional
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    @DSTransactional
    public void theSame() {
        UserFiveDO one = new UserFiveDO();
        one.setName("sameOne").setAge(1).setEmail("sameOne@qq.com").setGender(GenderEnum.MAN);
        fiveUserMapper.insert(one);

        UserFiveDO two = new UserFiveDO();
        two.setName("sameTwo").setAge(1).setEmail("sameTwo@163.com").setGender(GenderEnum.WOMAN);
        fiveUserMapper.insert(two);

        //模拟异常
        int i = 1 / 0;
    }

    @Override
    @DSTransactional
    public void notAlike() {
        UserFiveDO one = new UserFiveDO();
        one.setName("notAlike").setAge(1).setEmail("notAlike@qq.com").setGender(GenderEnum.MAN);
        fiveUserMapper.insert(one);

        myOrderMapper.deleteById(2L);

        //模拟异常
//        int i = 1 / 0;
    }

    @Override
    public void manual() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            DataSource datasourceBean = transactionManager.getDataSource();
            System.out.println("dataSource = " + datasourceBean);

            UserFiveDO one = new UserFiveDO();
            one.setName("manual").setAge(18).setEmail("manual@qq.com").setGender(GenderEnum.MAN);
            fiveUserMapper.insert(one);

            //todo 这里没有进行数据源切换，所以尽量避免手动管理事务，还是直接用注解吧
            MyOrder order = new MyOrder();
            order.setUserId(2L).setGoodsId(2L).setPrice(new BigDecimal(1));
            myOrderMapper.insert(order);

            //模拟异常
            int i = 1 / 0;

            transactionManager.commit(status);
        } catch (TransactionException e) {
            transactionManager.rollback(status);
            System.err.println("异常 ==> 进行事务回滚");
        }
    }

}
