package com.nova.tools.utils.hutool.core.net;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ReUtil;
import org.junit.jupiter.api.Test;

import java.net.HttpCookie;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * NetUtil单元测试
 *
 * @author:Looly
 */
public class NetUtilTest {

    @Test
    public void getLocalhostStrTest() {
        String localhost = NetUtil.getLocalhostStr();
        Assert.notNull(localhost);
    }

    @Test
    public void getLocalhostTest() {
        InetAddress localhost = NetUtil.getLocalhost();
        Assert.notNull(localhost);
    }

    @Test
    public void getLocalMacAddressTest() {
        String macAddress = NetUtil.getLocalMacAddress();
        Assert.notNull(macAddress);

        // 验证MAC地址正确
        boolean match = ReUtil.isMatch(PatternPool.MAC_ADDRESS, macAddress);
        Assert.isTrue(match);
    }

    @Test
    public void longToIpTest() {
        String ipv4 = NetUtil.longToIpv4(2130706433L);
        Assert.equals("127.0.0.1", ipv4);
    }

    @Test
    public void ipToLongTest() {
        long ipLong = NetUtil.ipv4ToLong("127.0.0.1");
        Assert.equals(2130706433L, ipLong);
    }

    @Test
    public void isUsableLocalPortTest() {
        Assert.isTrue(NetUtil.isUsableLocalPort(80));
    }

    @Test
    public void parseCookiesTest() {
        String cookieStr = "cookieName=\"cookieValue\";Path=\"/\";Domain=\"cookiedomain.com\"";
        final List<HttpCookie> httpCookies = NetUtil.parseCookies(cookieStr);
        Assert.equals(1, httpCookies.size());

        final HttpCookie httpCookie = httpCookies.get(0);
        Assert.equals(0, httpCookie.getVersion());
        Assert.equals("cookieName", httpCookie.getName());
        Assert.equals("cookieValue", httpCookie.getValue());
        Assert.equals("/", httpCookie.getPath());
        Assert.equals("cookiedomain.com", httpCookie.getDomain());
    }

    @Test
    public void getLocalHostNameTest() {
        Assert.notNull(NetUtil.getLocalHostName());
    }

    @Test
    public void pingTest() {
        Assert.isTrue(NetUtil.ping("127.0.0.1"));
    }

    @Test
    public void isOpenTest() {
        InetSocketAddress address = new InetSocketAddress("www.hutool.cn", 443);
        Assert.isTrue(NetUtil.isOpen(address, 200));
    }

    @Test
    public void getDnsInfoTest() {
        final List<String> txt = NetUtil.getDnsInfo("hutool.cn", "TXT");
        Console.log(txt);
    }

    @Test
    public void isInRangeTest() {
        Assert.isTrue(NetUtil.isInRange("114.114.114.114", "0.0.0.0/0"));
        Assert.isTrue(NetUtil.isInRange("192.168.3.4", "192.0.0.0/8"));
        Assert.isTrue(NetUtil.isInRange("192.168.3.4", "192.168.0.0/16"));
        Assert.isTrue(NetUtil.isInRange("192.168.3.4", "192.168.3.0/24"));
        Assert.isTrue(NetUtil.isInRange("192.168.3.4", "192.168.3.4/32"));
        Assert.isFalse(NetUtil.isInRange("8.8.8.8", "192.0.0.0/8"));
        Assert.isFalse(NetUtil.isInRange("114.114.114.114", "192.168.3.4/32"));
    }

}
