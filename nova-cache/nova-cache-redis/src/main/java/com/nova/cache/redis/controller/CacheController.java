package com.nova.cache.redis.controller;

import com.nova.common.core.model.pojo.BaseResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: wzh
 * @description: 缓存controller
 * @date: 2024/01/15 09:39
 */
@Slf4j(topic = "CacheController")
@RestController
@RequestMapping("/api")
public class CacheController {

    static List<BaseResDTO> list = new ArrayList<>();

    static {
        list.add(BaseResDTO.builder().operator("1").createTime(new Date()).build());
        list.add(BaseResDTO.builder().operator("2").createTime(new Date()).build());
        list.add(BaseResDTO.builder().operator("3").createTime(new Date()).build());
    }

    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @Cacheable(value = "getList", key = "#id", unless = "#result == null")
    public List<BaseResDTO> getList(String id) {
        System.err.println("id = " + id);
        return list;
    }

    /**
     * allEntries 清空所有缓存
     */
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @CacheEvict(cacheNames = "getList", allEntries = true)
    public void update(String id) {
        System.err.println("id = " + id);
    }

}
