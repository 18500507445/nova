package com.nova.tools.utils.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.cookie.GlobalCookieManager;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import java.net.HttpCookie;
import java.util.List;

public class Issue2658Test {

    @SuppressWarnings("resource")
    @Test
    @Ignore
    public void getWithCookieTest() {
        HttpRequest.get("https://www.baidu.com/").execute();
        final List<HttpCookie> cookies = GlobalCookieManager.getCookieManager().getCookieStore().getCookies();
        Console.log("###" + cookies);
        HttpRequest.get("https://www.baidu.com/").execute();
    }
}
