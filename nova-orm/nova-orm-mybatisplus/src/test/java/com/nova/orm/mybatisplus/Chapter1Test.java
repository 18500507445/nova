package com.nova.orm.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nova.orm.mybatisplus.chapter1.OneUserMapper;
import com.nova.orm.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author: wzh
 * @description 第一章测试类：入门篇，pagehelper的分页
 * @date: 2023/06/15 20:01
 */
@SpringBootTest
public class Chapter1Test {

    @Autowired
    private OneUserMapper oneUserMapper;

    /**
     * 关闭 pageHelper启动banner
     */
    static {
        System.setProperty("pagehelper.banner", "false");
    }

    @Test
    public void selectList() {
        Page<UserDO> page = PageHelper.startPage(1, 10);
        List<UserDO> userDOList = oneUserMapper.selectList(null);
        PageInfo<UserDO> pageInfo = new PageInfo<>(userDOList);
        String jsonStr = JSONUtil.toJsonStr(pageInfo);
        System.err.println("jsonStr = " + jsonStr);
        System.err.println("page = " + page);
    }

    @Test
    public void pagingTwo() {
        PageHelper.offsetPage(1, 10);
    }

    @Test
    public void pagingThree() {
        Page<UserDO> page = PageHelper.startPage(1, 10)
                .doSelectPage(() -> oneUserMapper.selectList(null));
        System.err.println("page = " + page);
        System.err.println("page.getPageNum() = " + page.getPageNum());
        System.err.println("page.getPageSize() = " + page.getPageSize());
        System.err.println("page.getPages() = " + page.getPages());
        System.err.println("page.getTotal() = " + page.getTotal());
        System.err.println("page.getCountColumn() = " + page.getCountColumn());

    }

    @Test
    public void pagingFour() {
        PageInfo<UserDO> pageInfo = PageHelper.startPage(1, 10)
                .doSelectPageInfo(() -> oneUserMapper.selectList(null));

        System.err.println("pageInfo = " + pageInfo);
    }

}
