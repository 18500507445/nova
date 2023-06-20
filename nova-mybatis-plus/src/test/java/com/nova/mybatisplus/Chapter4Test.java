package com.nova.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nova.mybatisplus.chapter4.FourUserMapper;
import com.nova.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description 第四章测试类：进阶篇
 * @date: 2023/06/15 20:01
 */
@SpringBootTest
public class Chapter4Test {

    @Resource
    private FourUserMapper fourUserMapper;

    @Test
    public void insert() {
        UserDO userDO = new UserDO();
        userDO.setName("Wai").setAge(99).setEmail("123456789@qq.com");
        int insert = fourUserMapper.insert(userDO);
        System.out.println("insert = " + insert);
    }

    /**
     * 使用分页插件查询
     */
    @Test
    public void limitQuery() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        //指定分页对象
        IPage<UserDO> page = new Page<>(2, 3);

        //执行查询
        fourUserMapper.selectPage(page, queryWrapper);

        //获取分页查询的信息
        System.out.println("currentPage = " + page.getCurrent());
        System.out.println("pageSize = " + page.getSize());
        System.out.println("totalPages = " + page.getPages());
        System.out.println("totalSizes = " + page.getTotal());
        System.out.println("result = " + JSONUtil.toJsonStr(page.getRecords()));
    }

    /**
     * 自定义分页
     */
    @Test
    public void customQuery() {
        IPage<UserDO> page = new Page<>(1, 2);
        fourUserMapper.selectByName(page, "tom");
        System.out.println("result = " + JSONUtil.toJsonStr(page.getRecords()));
    }


}
