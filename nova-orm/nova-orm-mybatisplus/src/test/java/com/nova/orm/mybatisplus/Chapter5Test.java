package com.nova.orm.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.nova.orm.mybatisplus.chapter5.FiveUserMapper;
import com.nova.orm.mybatisplus.chapter5.FiveUserService;
import com.nova.orm.mybatisplus.chapter5.GenderEnum;
import com.nova.orm.mybatisplus.chapter5.MyOrderMapper;
import com.nova.orm.mybatisplus.entity.MyOrder;
import com.nova.orm.mybatisplus.entity.UserFiveDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Collections;
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
    private MyOrderMapper myOrderMapper;

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


    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://47.100.174.176:3306/study?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8", "root", "@wangzehui123")
                .globalConfig(builder -> builder
                        // 设置作者
                        .author("wzh")
                        // 开启 swagger 模式
//                        .enableSwagger()
                        // 指定输出目录
                        .outputDir("/Users/wangzehui/Downloads"))
                .packageConfig(builder -> builder
                        // 设置父包名
                        .parent("com.nova.mybatisplus")
                        // 设置父包模块名
//                        .moduleName("system")
                        // 设置mapperXml生成路径
                        .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/wangzehui/Downloads/com/nova/mybatisplus")))
                .strategyConfig(builder -> builder
                        // 设置需要生成的表名
                        .addInclude("user")
                        // 设置过滤表前缀
                        .addTablePrefix("t_", "c_"))
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }


    /**
     * 多数据源查询
     */
    @Test
    public void dynamicDataSource() {
        MyOrder myOrder = myOrderMapper.selectById(1L);
        UserFiveDO result = fiveUserMapper.selectById(1671354213734621185L);
        System.err.println("myOrder = " + JSONUtil.toJsonStr(myOrder));
        System.err.println("result = " + JSONUtil.toJsonStr(result));
    }

    /**
     * 手动开启事务
     */
    @Test
    public void testTransaction() {
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

    /**
     * 多数据源事务：@DSTransactional，直接作用于方法上
     */
    @Test
    public void testDsTransaction() {
//        fiveUserService.theSame();
        fiveUserService.notAlike();
    }


}
