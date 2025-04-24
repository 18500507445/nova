package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.URLUtil;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * {@link URLUtil} URLUtil单元测试
 *
 * @author: Looly
 */
public class URLUtilTest {

    @Test
    public void normalizeTest() {
        // issue#I25MZL，多个/被允许
        String url = "http://www.hutool.cn//aaa/bbb";
        String normalize = URLUtil.normalize(url);
        Assert.equals("http://www.hutool.cn//aaa/bbb", normalize);

        url = "www.hutool.cn//aaa/bbb";
        normalize = URLUtil.normalize(url);
        Assert.equals("http://www.hutool.cn//aaa/bbb", normalize);
    }

    @Test
    public void normalizeTest2() {
        String url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
        String normalize = URLUtil.normalize(url);
        Assert.equals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);

        url = "www.hutool.cn//aaa/bbb?a=1&b=2";
        normalize = URLUtil.normalize(url);
        Assert.equals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);
    }

    @Test
    public void normalizeTest3() {
        String url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
        String normalize = URLUtil.normalize(url, true);
        Assert.equals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);

        url = "www.hutool.cn//aaa/bbb?a=1&b=2";
        normalize = URLUtil.normalize(url, true);
        Assert.equals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);

        url = "\\/www.hutool.cn//aaa/bbb?a=1&b=2";
        normalize = URLUtil.normalize(url, true);
        Assert.equals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);
    }

    @Test
    public void normalizeIpv6Test() {
        String url = "http://[fe80::8f8:2022:a603:d180]:9439";
        String normalize = URLUtil.normalize("http://[fe80::8f8:2022:a603:d180]:9439", true);
        Assert.equals(url, normalize);
    }

    @Test
    public void formatTest() {
        String url = "//www.hutool.cn//aaa/\\bbb?a=1&b=2";
        String normalize = URLUtil.normalize(url);
        Assert.equals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);
    }

    @Test
    public void getHostTest() throws MalformedURLException {
        String url = "https://www.hutool.cn//aaa/\\bbb?a=1&b=2";
        String normalize = URLUtil.normalize(url);
        URI host = URLUtil.getHost(new URL(normalize));
        Assert.equals("https://www.hutool.cn", host.toString());
    }

    @Test
    public void encodeTest() {
        String body = "366466 - 副本.jpg";
        String encode = URLUtil.encode(body);
        Assert.equals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode);
        Assert.equals(body, URLUtil.decode(encode));

        String encode2 = URLUtil.encodeQuery(body);
        Assert.equals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode2);
    }

    @Test
    public void encodeQueryPlusTest() {
        String body = "+";
        String encode2 = URLUtil.encodeQuery(body);
        Assert.equals("+", encode2);
    }

    @Test
    public void getPathTest() {
        String url = " http://www.aaa.bbb/search?scope=ccc&q=ddd";
        String path = URLUtil.getPath(url);
        Assert.equals("/search", path);
    }
}
