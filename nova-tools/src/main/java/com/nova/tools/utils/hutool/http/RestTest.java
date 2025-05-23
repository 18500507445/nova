package com.nova.tools.utils.hutool.http;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

/**
 * Rest类型请求单元测试
 *
 * @author: looly
 */
public class RestTest {

    @Test
    public void contentTypeTest() {
        HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")//
                .body(JSONUtil.createObj()
                        .set("aaa", "aaaValue")
                        .set("键2", "值2").toString());
        Assert.equals("application/json;charset=UTF-8", request.header("Content-Type"));
    }

    @Test
    public void postTest() {
        HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")//
                .body(JSONUtil.createObj()
                        .set("aaa", "aaaValue")
                        .set("键2", "值2").toString());
        Console.log(request.execute().body());
    }

    @Test
    public void postTest2() {
        String result = HttpUtil.post("http://localhost:8090/rest/restTest/", JSONUtil.createObj()//
                .set("aaa", "aaaValue")
                .set("键2", "值2").toString());
        Console.log(result);
    }

    @Test
    public void getWithBodyTest() {
        HttpRequest request = HttpRequest.get("http://localhost:8888/restTest")//
                .header(Header.CONTENT_TYPE, "application/json")
                .body(JSONUtil.createObj()
                        .set("aaa", "aaaValue")
                        .set("键2", "值2").toString());
        Console.log(request.execute().body());
    }

    @Test
    public void getWithBodyTest2() {
        HttpRequest request = HttpRequest.get("https://ad.oceanengine.com/open_api/2/advertiser/info/")//
                // Charles代理
                .setHttpProxy("localhost", 8888)
                .header("Access-Token", "")
                .body(JSONUtil.createObj()
                        .set("advertiser_ids", new Long[]{1690657248243790L})
                        .set("fields", new String[]{"id", "name", "status"}).toString());
        Console.log(request);
        Console.log(request.execute().body());
    }
}
