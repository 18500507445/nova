package com.nova.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nova.mybatisplus.chapter3.ThreeUserMapper;
import com.nova.mybatisplus.chapter3.ThreeUserService;
import com.nova.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: wzh
 * @description 第三章测试类：进阶篇
 * @date: 2023/06/15 20:01
 */
@SpringBootTest
public class Chapter3Test {

    @Resource
    private ThreeUserService thirdUserService;

    @Resource
    private ThreeUserMapper threeUserMapper;

    /**
     * 等值查询eq，不等查询ne
     */
    @Test
    public void eq() {
        //1.创建条件查询对象
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();

        //2.设置相等查询条件，指定查询的字段和匹配的值
        queryWrapper.eq("name", "Tom");

        //3.进行条件查询
        UserDO userDO = threeUserMapper.selectOne(queryWrapper);
        System.out.println("userDO = " + JSONUtil.toJsonStr(userDO));
    }

    /**
     * 多条件相等查询HashMap
     */
    @Test
    public void mapEq() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", "Sandy");
        hashMap.put("age", null);

        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        //null2IsNull设置为false，age为null，sql不作为拼接条件
        queryWrapper.allEq(hashMap, false);
        UserDO userDO = threeUserMapper.selectOne(queryWrapper);
        System.out.println("userDO = " + JSONUtil.toJsonStr(userDO));
    }

    /**
     * 升级版：拼接条件写错了，导致查询错误。使用LambdaWrapper限制实体类中的字段
     */
    @Test
    public void lambdaEq() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getName, "Tom");
        queryWrapper.eq(UserDO::getAge, 28);
        UserDO userDO = threeUserMapper.selectOne(queryWrapper);
        System.out.println("userDO = " + JSONUtil.toJsonStr(userDO));
    }

    /**
     * 范围查询
     */
    @Test
    public void rangeQuery() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        //大于
        queryWrapper.gt(UserDO::getAge, 18);

        //大于等于
        queryWrapper.ge(UserDO::getAge, 18);

        //小于
        queryWrapper.lt(UserDO::getAge, 60);

        //小于等于
        queryWrapper.le(UserDO::getAge, 60);

        //区间
        queryWrapper.between(UserDO::getAge, 18, 60);

        //不再区间
        queryWrapper.notBetween(UserDO::getAge, 60, 100);
    }

    /**
     * 模糊查询
     */
    @Test
    public void likeQuery() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        //全模糊匹配 %o%
        queryWrapper.like(UserDO::getName, "o");

        //左模糊匹配 %o
        queryWrapper.likeLeft(UserDO::getName, "o");

        //右模糊匹配 o%
        queryWrapper.likeLeft(UserDO::getName, "o");

        //notLike
        queryWrapper.notLike(UserDO::getName, "o");
    }

    /**
     * 判空查询
     */
    @Test
    public void isNull() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        //空的数据
        queryWrapper.isNull(UserDO::getName);

        //不是空的数据
        queryWrapper.isNotNull(UserDO::getName);
    }


}
