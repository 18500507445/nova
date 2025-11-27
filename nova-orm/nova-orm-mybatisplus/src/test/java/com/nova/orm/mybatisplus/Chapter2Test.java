package com.nova.orm.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nova.orm.mybatisplus.chapter2.TwoUserMapper;
import com.nova.orm.mybatisplus.chapter2.TwoUserService;
import com.nova.orm.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: wzh
 * @description: 第二章测试类：基础篇
 * @date: 2023/06/16 09:35
 */
@SpringBootTest
public class Chapter2Test {

    @Autowired
    private TwoUserMapper twoUserMapper;

    @Autowired
    private TwoUserService twoUserService;

    static {
        System.setProperty("pagehelper.banner", "false");
    }

    /**
     * Mapper进行简单的增删改查
     */
    @Test
    public void insert() {
        UserDO userDO = new UserDO();
        userDO.setId(6L).setName("Wzh").setAge(28).setEmail("188508540@qq.com");
        int insert = twoUserMapper.insert(userDO);
        System.err.println("insert = " + insert);
    }

    @Test
    public void delete() {
        twoUserMapper.deleteById(1669614258473791490L);
    }

    @Test
    public void update() {
        UserDO userDO = new UserDO();
        userDO.setId(6L).setName("Wzh").setAge(29).setEmail("188508540@qq.com");
        twoUserMapper.updateById(userDO);
    }

    @Test
    public void query() {
        UserDO userDO = twoUserMapper.selectById(6L);
        System.err.println("jsonStr = " + JSONUtil.toJsonStr(userDO));
    }


    /***
     * 简化Service后，使用Service进行增删改查
     */
    @Test
    public void testInsert() {
        UserDO userDO = new UserDO();
        userDO.setId(7L).setName("Cf").setAge(26).setEmail("188508540@qq.com");
        boolean save = twoUserService.save(userDO);
        System.err.println("save = " + save);
    }

    @Test
    public void testDelete() {
        boolean delete = twoUserService.removeById(7L);
        System.err.println("delete = " + delete);
    }

    @Test
    public void testUpdate() {
        UserDO userDO = new UserDO();
        userDO.setId(7L).setName("Wzh").setAge(27).setEmail("188508540@qq.com");
        twoUserService.updateById(userDO);
    }

    @Test
    public void testQuery() {
        UserDO userDO = twoUserService.getById(7L);
        System.err.println("jsonStr = " + JSONUtil.toJsonStr(userDO));
    }


    /**
     * 自定义Mapper接口
     */
    @Test
    public void queryByName() {
        UserDO userDO = twoUserMapper.selectByName("Tom");
        System.err.println("jsonStr = " + JSONUtil.toJsonStr(userDO));
    }


    /**
     * 内置的分页插件
     */
    @Test
    public void queryListByPage() {
        Page<UserDO> page = twoUserMapper.selectPage(new Page<>(1, 5), null);
        System.err.println("jsonStr = " + JSONUtil.toJsonStr(page));
    }

}
