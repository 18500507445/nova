package com.nova.search.elasticsearch;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.search.elasticsearch.annotation.BaseEsEntity;
import com.nova.search.elasticsearch.entity.User;
import com.nova.search.elasticsearch.manage.ElasticsearchService;
import com.nova.search.elasticsearch.utils.EsMapUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: wzh
 * @description: es工具类测试
 * @date: 2024/04/28 16:10
 */
@Slf4j
@SpringBootTest
public class ElasticsearchServiceTest {

    @Resource
    private ElasticsearchService elasticsearchService;

    @Test
    public void save() {
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUserName("张三_" + i);
            user.setPassword("password_" + i);
            user.setCreateTime(new Date());
            elasticsearchService.save(user);
        }
    }

    @Test
    public void findById() {
        User user = elasticsearchService.findById(1L, User.class);
        System.out.println("user = " + user);
    }

    @Test
    public void findAllById() {
        List<Integer> idList = Arrays.asList(1, 2, 3, 4, 5);
        List<User> list = elasticsearchService.findAllById(idList, User.class);
        System.out.println("list = " + JSONUtil.toJsonStr(list));
    }

    @Test
    public void findAll() {
        List<User> list = elasticsearchService.findAll(User.class);
        System.out.println("list = " + JSONUtil.toJsonStr(list));
    }

    @Test
    public void delete() {
        List<Integer> idList = Arrays.asList(1, 2, 3, 4, 5);
        elasticsearchService.deleteAllById(idList, User.class);
    }


    @Test
    public void queryList() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        List<BaseEsEntity.QueryRelation<String>> queryList = Stream.of(new BaseEsEntity.QueryRelation<>("password_1", BaseEsEntity.SHOULD),
                new BaseEsEntity.QueryRelation<>("password_2", BaseEsEntity.SHOULD)).collect(Collectors.toList());
        user.setFieldQueryMap(new EsMapUtil().put(User::getPassword, queryList));
        //排序查询
        user.setOrderMap(new EsMapUtil().put(User::getId, SortOrder.DESC));
        List<User> list = elasticsearchService.queryList(user);
        System.out.println("list = " + JSONObject.toJSONString(list));
    }


}
