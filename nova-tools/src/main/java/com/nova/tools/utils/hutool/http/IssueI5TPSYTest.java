package com.nova.tools.utils.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;

import java.net.HttpCookie;

public class IssueI5TPSYTest {
    @Test
    public void redirectTest() {
        final String url = "https://bsxt.gdzwfw.gov.cn/UnifiedReporting/auth/newIndex";
        final HttpResponse res = HttpUtil.createGet(url).setFollowRedirects(true)
                .cookie(new HttpCookie("iPlanetDirectoryPro", "123"))
                .execute();
        Console.log(res.body());
    }
}
