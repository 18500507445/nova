package com.nova.orm.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.nova.orm.mybatisplus.chapter5.FiveUserMapper;
import com.nova.orm.mybatisplus.chapter5.FiveUserService;
import com.nova.orm.mybatisplus.chapter5.GenderEnum;
import com.nova.orm.mybatisplus.chapter5.OrderMapper;
import com.nova.orm.mybatisplus.entity.Order;
import com.nova.orm.mybatisplus.entity.UserFiveDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wzh
 * @description 第五章测试类：拓展篇
 * @date: 2023/06/21 10:28
 */
@SpringBootTest
public class Chapter5Test {

    @Autowired
    private FiveUserMapper fiveUserMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FiveUserService fiveUserService;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    static {
        System.setProperty("pagehelper.banner", "false");
    }

    @Test
    public void logicDelete() {
        fiveUserMapper.deleteById(5);
    }

    @Test
    public void enumTest() {
        UserFiveDO userDO = new UserFiveDO();
        userDO.setName("Liu").setAge(29).setEmail("12314114@qq.com").setGender(GenderEnum.MAN);
        fiveUserMapper.insert(userDO);
    }

    /**
     * 插入转Json，查询转Map
     */
    @Test
    public void contactInsert() {
        UserFiveDO userDO = new UserFiveDO();
        userDO.setName("Huang").setAge(55).setEmail("313131314114@qq.com").setGender(GenderEnum.WOMAN);

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("tel", "60991234");
        hashMap.put("phone", "18500225555");
        userDO.setContact(hashMap);
        fiveUserMapper.insert(userDO);
    }

    @Test
    public void contactQuery() {
        UserFiveDO result = fiveUserMapper.selectById(1671354213734621185L);
        System.err.println("result = " + JSONUtil.toJsonStr(result));
    }

    @Test
    public void testFillInsert() {
        UserFiveDO userDO = new UserFiveDO();
        userDO.setName("Wang").setAge(22).setEmail("4431313114@qq.com").setGender(GenderEnum.MAN);
        fiveUserMapper.insert(userDO);
    }

    @Test
    public void testFillUpdate() {
        UserFiveDO userDO = new UserFiveDO();
        userDO.setId(1671359331620130817L).setAge(28);
        fiveUserMapper.updateById(userDO);
    }

    @Test
    public void updateAll() {
        UserFiveDO userDO = new UserFiveDO();
        userDO.setGender(GenderEnum.MAN);
        fiveUserService.saveOrUpdate(userDO, null);
    }


    /**
     * 多数据源查询
     */
    @Test
    public void dynamicDataSource() {
        Order order = orderMapper.selectById(1L);
        UserFiveDO result = fiveUserMapper.selectById(1671354213734621185L);
        System.err.println("myOrder = " + JSONUtil.toJsonStr(order));
        System.err.println("result = " + JSONUtil.toJsonStr(result));
    }

    /**
     * 手动开启事务
     */
    @Test
    public void testTransaction() {
        fiveUserService.defaultCase();
    }

    /**
     * 多数据源事务：@DSTransactional，直接作用于方法上
     * (1)同库数据源
     * (2)不同库数据源
     */
    @Test
    public void testDsTransaction() {
//        fiveUserService.theSame();
        fiveUserService.notAlike();
    }

    /**
     * mp手动管理事务，跨库不行
     */
    @Test
    public void testManualTest() {
        fiveUserService.manual();
    }


}
