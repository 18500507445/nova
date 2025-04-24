package com.nova.orm.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nova.orm.mybatisplus.chapter3.ThreeUserMapper;
import com.nova.orm.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * @author: wzh
 * @description: 第三章测试类：进阶篇
 * @date: 2023/06/15 20:01
 */
@SpringBootTest
public class Chapter3Test {

    @Autowired
    private ThreeUserMapper threeUserMapper;

    static {
        System.setProperty("pagehelper.banner", "false");
    }

    /**
     * 等值查询eq，不等查询ne
     */
    @Test
    public void eq() {
        //1.创建条件查询对象
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();

        //2.设置相等查询条件，指定查询的字段和匹配的值
        queryWrapper.eq("name", "Tom").last("limit 1");

        //3.进行条件查询
        UserDO userDO = threeUserMapper.selectOne(queryWrapper);
        System.err.println("userDO = " + JSONUtil.toJsonStr(userDO));
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
        System.err.println("userDO = " + JSONUtil.toJsonStr(userDO));
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
        System.err.println("userDO = " + JSONUtil.toJsonStr(userDO));
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

    /**
     * in查询
     */
    @Test
    public void inQuery() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        List<Integer> ageList = new ArrayList<>();
        Collections.addAll(ageList, 18, 19, 20);

        //in查询
        queryWrapper.in(UserDO::getAge, ageList);

        //not in查询
        queryWrapper.notIn(UserDO::getAge, ageList);

        //in sql
        queryWrapper.inSql(UserDO::getAge, "18,19,20");

        //not inSql
        queryWrapper.notInSql(UserDO::getAge, "18,19,20");

        //子查询
        queryWrapper.inSql(UserDO::getAge, "select age from user");
    }

    /**
     * 分组、聚合、排序查询
     * @deprecated :也可以切换成lambda写法
     */
    @Test
    public void groupQuery() {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();

        //分组字段
        queryWrapper.groupBy("age");

        //查询字段
        queryWrapper.select("age,count(1) as num");

        //聚合删选条件
        queryWrapper.having("num >= 1");

        //排序，备注：多个字段排序多写一个orderBy既可
        queryWrapper.orderBy(true, true, "age");

        //排序写法二
        LambdaQueryWrapper<UserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(UserDO::getAge);

        //因为查询出来的字段和实体不匹配，所以用Map接收
        List<Map<String, Object>> maps = threeUserMapper.selectMaps(queryWrapper);
        System.err.println("maps = " + maps);
    }

    /**
     * 条件拼接
     */
    @Test
    public void func() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();

        //根据不同的情况来选择不同的查询条件
        queryWrapper.func(userDOLambdaQueryWrapper -> {
            if (true) {
                userDOLambdaQueryWrapper.eq(UserDO::getAge, 18);
            } else {
                userDOLambdaQueryWrapper.ne(UserDO::getAge, 18);
            }
        });
    }

    /**
     * 逻辑and、or查询
     */
    @Test
    public void logicalQuery() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();

        //直接拼接 age > 18 and age < 60
        queryWrapper.gt(UserDO::getAge, 18).lt(UserDO::getAge, 60);

        //直接or拼接，age > 26 or age < 22
        queryWrapper.gt(UserDO::getAge, 26).or().lt(UserDO::getAge, 22);

        //and和or嵌套拼接，name = 'Tom' and (age > 26 or age < 22)
        queryWrapper.eq(UserDO::getName, "Tom")
                .and(i -> i.gt(UserDO::getAge, 26).or().lt(UserDO::getAge, 22));

        //直接嵌套 name = 'Tom' and age != 22
        queryWrapper.nested(i -> i.eq(UserDO::getName, "Tom").ne(UserDO::getAge, 22));

    }

    /**
     * 自定义sql
     */
    @Test
    public void applyQuery() {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        //直接拼接sql
        queryWrapper.apply("id = 1");

        //分页查询
        queryWrapper.last("limit 0,2");

        //exists查询
        queryWrapper.exists("select id from user where age = 18");

        //not exists查询
        queryWrapper.notExists("select id from user where age = 18");

        //返回字段 id,name
        queryWrapper.select(UserDO::getId, UserDO::getName);
    }

}
