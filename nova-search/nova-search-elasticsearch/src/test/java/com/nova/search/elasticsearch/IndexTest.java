package com.nova.search.elasticsearch;

import cn.hutool.core.lang.Console;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description: 索引工具类
 * @date: 2024/04/28 09:26
 */
@Slf4j
@SpringBootTest
public class IndexTest {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public static final String INDEX_NAME = "test_create_index";

    //创建索引 + 索引是否存在
    @Test
    public void createTest() {
        IndexOperations indexOps = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(INDEX_NAME));
        if (!indexOps.exists()) {
            indexOps.create();
            Console.log("索引创建成功");
        } else {
            Console.log("索引已存在");
        }
    }

    //删除索引
    @Test
    public void deleteTest() {
        boolean delete = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(INDEX_NAME)).delete();
        Console.log("delete {} ", delete);
    }

}
