package com.nova.search.solr.controller;

import cn.hutool.json.JSONUtil;
import com.nova.search.solr.repository.Demo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: wzh
 * @description: solr
 * @date: 2025/10/30 15:01
 */
@Slf4j(topic = "SolrController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SolrController {

    private final SolrClient solrClient;

    @GetMapping("/query")
    public void query() {
        SolrQuery query = new SolrQuery();
        query.set("defType", "edismax");
        query.set("mm", "100%");

        //分页
        query.setStart(1);
        query.setRows(50);

        //排序
        query.addSort("update_time", SolrQuery.ORDER.desc);
        query.addSort("db_id", SolrQuery.ORDER.desc);

        try {
            QueryResponse resp = solrClient.query("ent_custom_pool", query);
            List<Demo> beans = resp.getBeans(Demo.class);
            String jsonStr = JSONUtil.toJsonStr(beans);
            log.error("json ===> {}", jsonStr);
        } catch (Exception e) {
            log.error("query异常：", e);
        }
    }

}
