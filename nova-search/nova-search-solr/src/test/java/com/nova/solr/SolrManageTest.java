package com.nova.solr;

import lombok.Data;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author: wzh
 * @description 添加和修改
 * @date: 2023/07/13 16:03
 */
@SpringBootTest
public class SolrManageTest {

    @Autowired
    private SolrClient solrClient;

    public static final String CORE_NAME = "testcore";

    /**
     * 连接方式一，最好写成单例
     */
    @Test
    public void clientA() throws SolrServerException, IOException {
        //1.指定solr服务器地址和实例名
        String url = "http://username:password@ip:port/xxx";

        //2.创建solr客户端对象，连接服务器
        SolrClient client = new HttpSolrClient.Builder(url)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();

        //3.创建文档对象
        SolrInputDocument doc = new SolrInputDocument();

        //4.创建域对象，将数据放入域中，将域放入文档对象中
        doc.addField("id", "1");
        doc.addField("name", "苹果手机");
        doc.addField("price", "9000");

        //5.将文档对象放入客户端对象中
        client.add(CORE_NAME, doc);

        //6.提交
        client.commit(CORE_NAME);
    }

    /**
     * 连接方式二，配置文件，注入SolrClient
     */
    @Test
    public void clientB() throws SolrServerException, IOException {
        solrClient.add(CORE_NAME, new SolrInputDocument());
        solrClient.commit(CORE_NAME);
    }

    @Test
    public void delete() throws SolrServerException, IOException {
        //id删除
        solrClient.deleteById("");
        //条件删除，*:* 代表查询所有
        solrClient.deleteByQuery("*:*");
    }

    @Data
    static class User {
        @Field("id")
        private String id;
        @Field("name")
        private String name;
    }

    /**
     * 简单查询
     */
    @Test
    public void simpleQuery() throws SolrServerException, IOException {
        SolrQuery solrQuery = new SolrQuery();
        //查询所有
        solrQuery.set("q", "*:*");
        //10条记录
        solrQuery.setRows(10);
        //设置回显内容
        solrQuery.addField("id");
        solrQuery.addField("name");
        //排序
        solrQuery.setSort("id", SolrQuery.ORDER.desc);

        //查询并返回响应结果
        QueryResponse response = solrClient.query(CORE_NAME, solrQuery);

        //获取文档集合
        SolrDocumentList documentList = response.getResults();

        //获取bean集合
        List<User> beans = response.getBeans(User.class);


        //结果总数
        long numFound = documentList.getNumFound();

        //遍历集合
        for (SolrDocument entries : documentList) {
            Object id = entries.get("id");
        }
    }

    /**
     * 简单添加
     */
    @Test
    public void simpleAdd() throws SolrServerException, IOException {
        User user = new User();
        user.setId("1");
        user.setName("wzh");
        solrClient.addBean(CORE_NAME, user);
        UpdateResponse response = solrClient.commit();
    }


}
