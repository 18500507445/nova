package com.nova.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author: wzh
 * @description 添加和修改
 * @date: 2023/07/13 16:03
 */
@SpringBootTest
public class SolrManageTest {

    @Resource
    private SolrClient solrClient;

    public static final String CORE_NAME = "testcore";

    /**
     * 连接方式一
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

    @Test
    public void query() {

    }


}
