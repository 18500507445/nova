package com.nova.tools.utils.hutool.core.net;

import cn.hutool.core.codec.PercentCodec;
import cn.hutool.core.net.RFC3986;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RFC3986Test {

    @Test
    public void encodeQueryTest() {
        String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=b", CharsetUtil.CHARSET_UTF_8);
        Assert.equals("a=b", encode);

        encode = RFC3986.QUERY_PARAM_VALUE.encode("a+1=b", CharsetUtil.CHARSET_UTF_8);
        Assert.equals("a+1=b", encode);
    }

    @Test
    public void encodeQueryPercentTest() {
        String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=%b", CharsetUtil.CHARSET_UTF_8);
        Assert.equals("a=%25b", encode);
    }

    @Test
    public void encodeQueryWithSafeTest() {
        String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=%25", CharsetUtil.CHARSET_UTF_8, '%');
        Assert.equals("a=%25", encode);
    }

    @Test
    public void encodeAllTest() throws UnsupportedEncodingException {
        String toVerifyText = "行吧行吧 cargo:1.0,\"Deta-ils:[{";
        final String encode = PercentCodec.of(RFC3986.UNRESERVED).setEncodeSpaceAsPlus(true).encode(toVerifyText, CharsetUtil.CHARSET_UTF_8);
        final String encodeJdk = URLEncoder.encode(toVerifyText, "UTF-8");
        Assert.equals(encode, encodeJdk);
    }
}
