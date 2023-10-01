package com.nova.orm.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.nova.orm.mybatisplus.chapter4.FourUserMapper;
import com.nova.orm.mybatisplus.entity.UserActiveRecordDO;
import com.nova.orm.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @author: wzh
 * @description 第四章测试类：进阶篇，分页插件、领域模型、simpleQuery
 * @date: 2023/06/15 20:01
 */
@SpringBootTest
public class Chapter4Test {

    @Autowired
    private FourUserMapper fourUserMapper;

    static {
        System.setProperty("pagehelper.banner", "false");
    }

    @Test
    public void insert() {
        UserDO userDO = new UserDO();
        userDO.setName("Wai").setAge(99).setEmail("123456789@qq.com");
        int insert = fourUserMapper.insert(userDO);
        System.err.println("insert = " + insert);
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
        System.err.println("currentPage = " + page.getCurrent());
        System.err.println("pageSize = " + page.getSize());
        System.err.println("totalPages = " + page.getPages());
        System.err.println("totalSizes = " + page.getTotal());
        System.err.println("result = " + JSONUtil.toJsonStr(page.getRecords()));
    }

    /**
     * 自定义分页
     */
    @Test
    public void customQuery() {
        IPage<UserDO> page = new Page<>(1, 2);
        fourUserMapper.selectByName(page, "tom");
        System.err.println("result = " + JSONUtil.toJsonStr(page.getRecords()));
    }


    /**
     * 领域模型-增删改查
     */
    @Test
    public void activeRecordInsert() {
        UserActiveRecordDO user = new UserActiveRecordDO();
        user.setName("Zhang").setAge(35).insert();
    }

    @Test
    public void activeRecordDelete() {
        UserActiveRecordDO user = new UserActiveRecordDO();
        user.setId(1671335418429210626L).deleteById();
    }

    @Test
    public void activeRecordUpdate() {
        UserActiveRecordDO user = new UserActiveRecordDO();
        user.setId(1671335418429210626L).setAge(36).updateById();
    }

    @Test
    public void activeRecordQuery() {
        UserActiveRecordDO user = new UserActiveRecordDO();
        user = user.setId(1671335418429210626L).selectById();
        System.err.println("result = " + JSONUtil.toJsonStr(user));
    }


    /**
     * simpleQuery操作list、map、group
     */
    @Test
    public void simpleQueryList() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getName, "Tom");
        //查询为Tom的idList
        List<Long> list = SimpleQuery.list(queryWrapper, UserDO::getId);
        System.err.println("list = " + JSONUtil.toJsonStr(list));
    }

    @Test
    public void simpleQueryList2() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getName, "Tom");
        //查询为Tom的nameList并进行转小写处理
        List<String> list = SimpleQuery.list(queryWrapper, UserDO::getName, userDO -> {
            //效果相同 Optional.of(userDO.getName()).map(String::toLowerCase).ifPresent(userDO::setName);
            userDO.setName(userDO.getName().toLowerCase());
        });
        System.err.println("list = " + JSONUtil.toJsonStr(list));
    }

    @Test
    public void simpleQueryMap() {
        //查询结果id为key，DO为value
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getName, "Tom");
        Map<Long, UserDO> userMap = SimpleQuery.keyMap(queryWrapper, UserDO::getId);
        System.err.println("userMap = " + JSONUtil.toJsonStr(userMap));
    }

    @Test
    public void simpleQueryMap2() {
        //查询结果任意组合key、value
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getName, "Tom");
        Map<Long, String> userMap = SimpleQuery.map(queryWrapper, UserDO::getId, UserDO::getName);
        System.err.println("userMap = " + JSONUtil.toJsonStr(userMap));
    }

    @Test
    public void simpleQueryGroup() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        Map<String, List<UserDO>> userGroup = SimpleQuery.group(queryWrapper, UserDO::getName);
        System.err.println("userGroup = " + JSONUtil.toJsonStr(userGroup));
    }


}
