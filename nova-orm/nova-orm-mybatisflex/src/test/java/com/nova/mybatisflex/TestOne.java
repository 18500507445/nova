package com.nova.mybatisflex;

import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.nova.mybatisflex.entity.Account;
import com.nova.mybatisflex.entity.AccountVO;
import com.nova.mybatisflex.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

import static com.nova.mybatisflex.entity.table.AccountTableDef.ACCOUNT;


/**
 * @author: wzh
 * @description 测试类
 * @date: 2023/07/30 14:09
 */
@SpringBootTest
public class TestOne {

    @Resource
    private AccountMapper accountMapper;

    @Test
    public void queryAll() {
        List<Account> list = accountMapper.selectAll();
        System.err.println("jsonStr = " + JSONUtil.toJsonStr(list));
    }

    /**
     * 自定义条件查询
     */
    @Test
    public void queryCustom() {
        String tableName = ACCOUNT.getTableName();
        System.err.println("tableName = " + tableName);

        QueryCondition eq = ACCOUNT.AGE.eq(18);
        System.err.println("eq = " + eq);

        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .from(ACCOUNT)
                .where(eq);
        Account account = accountMapper.selectOneByQuery(queryWrapper);
        System.err.println("account = " + account);

        //直接转换类
        AccountVO accountVO = accountMapper.selectOneByQueryAs(queryWrapper, AccountVO.class);
        System.err.println("accountVO = " + accountVO);
    }

    /**
     * 1.单条插入成功后，实体类带有id
     * 2.Selective方法忽略null值
     * 3.自定义主键，使用WithPk
     * 4.批量插入，按照字段最多的对象构建，不忽略null值
     */
    @Test
    public void insertTest() {
        int rows;
        Account account = new Account();
        account.setUserName("赵四").setAge(8).setBirthday(new Date());

        //忽略null，一般建议用这个
        rows = accountMapper.insertSelective(account);

        //不忽略null
        rows = accountMapper.insert(account);

        //手动设置是否忽略null值
        rows = accountMapper.insert(account, false);

        //WithPk --> 带有主键的插入
        account.setId(99L);
        rows = accountMapper.insertWithPk(account);

        //插入或者更新，多线程有问题，不建议使用
        rows = accountMapper.insertOrUpdateSelective(account);

        //批量插入，按照字段最多的对象构建，不忽略null值
        rows = accountMapper.insertBatch(new ArrayList<>());

        System.err.println(rows);
    }

    /**
     * 删除（禁止全表）
     */
    @Test
    public void deleteTest() {
        int rows = 0;
        //byId删除
        rows += accountMapper.deleteById(3L);

        //构造map删除
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", 4);
        rows += accountMapper.deleteByMap(hashMap);

        //构造whereConditions删除
        rows += accountMapper.deleteByCondition(ACCOUNT.ID.eq(5));

        //构造queryWrapper删除
        rows += accountMapper.deleteByQuery(QueryWrapper.create().where(ACCOUNT.ID.eq(6)));

        //批量删除
        rows += accountMapper.deleteBatchByIds(Arrays.asList(7, 8));

        System.err.println(rows);
    }

    /**
     * 更新数据（禁止全表），id不能为null，自动忽略null
     */
    @Test
    public void updateTest() {
        Account account = new Account();
        account.setId(4L).setAge(18).setBirthday(new Date());
        accountMapper.update(account);

        //age = age + 1
        accountMapper.updateByCondition(ACCOUNT.AGE, 1, QueryWrapper.create().where(ACCOUNT.ID.eq(4)));
    }

    @Test
    public void demoA() {

    }

    @Test
    public void demoB() {

    }


    @Test
    public void demoC() {

    }





}
