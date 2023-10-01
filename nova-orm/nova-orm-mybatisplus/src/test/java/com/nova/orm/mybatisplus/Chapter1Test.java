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
 * @description 第一章测试类：入门篇
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
        PageHelper.startPage(1, 10);
        List<UserDO> userDOList = oneUserMapper.selectList(null);
        PageInfo<UserDO> pageInfo = new PageInfo<>(userDOList);
        String jsonStr = JSONUtil.toJsonStr(pageInfo);
        System.err.println("jsonStr = " + jsonStr);
    }

    @Test
    public void pagingTwo() {
        PageHelper.offsetPage(1, 10);
    }

    @Test
    public void pagingThree() {
        Page<UserDO> page = PageHelper.startPage(1, 10)
                .doSelectPage(() -> oneUserMapper.selectList(null));
    }

    @Test
    public void pagingFour() {
        PageInfo<UserDO> pageInfo = PageHelper.startPage(1, 10)
                .doSelectPageInfo(() -> oneUserMapper.selectList(null));
    }


}
