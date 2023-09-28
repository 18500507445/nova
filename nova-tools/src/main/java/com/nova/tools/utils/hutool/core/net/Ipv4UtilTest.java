package com.nova.tools.utils.hutool.core.net;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.Ipv4Util;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Ipv4UtilTest {

    @Test
    public void getMaskBitByMaskTest() {
        final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
        Assert.equals(24, maskBitByMask);
    }

    //@Test
    //public void getMaskBitByIllegalMaskTest() {
    //	ThrowingRunnable getMaskBitByMaskRunnable = () -> Ipv4Util.getMaskBitByMask("255.255.0.255");
    //	Assert.assertThrows("非法掩码测试", IllegalArgumentException.class, getMaskBitByMaskRunnable);
    //}

    @Test
    public void getMaskByMaskBitTest() {
        final String mask = Ipv4Util.getMaskByMaskBit(24);
        Assert.equals("255.255.255.0", mask);
    }

    @Test
    public void longToIpTest() {
        String ip = "192.168.1.255";
        final long ipLong = Ipv4Util.ipv4ToLong(ip);
        String ipv4 = Ipv4Util.longToIpv4(ipLong);
        Assert.equals(ip, ipv4);
    }

    @Test
    public void getEndIpStrTest() {
        String ip = "192.168.1.1";
        final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
        final String endIpStr = Ipv4Util.getEndIpStr(ip, maskBitByMask);
        Assert.equals("192.168.1.255", endIpStr);
    }

    @Test
    public void listTest() {
        int maskBit = Ipv4Util.getMaskBitByMask("255.255.255.0");
        final List<String> list = Ipv4Util.list("192.168.100.2", maskBit, false);
        Assert.equals(254, list.size());
    }

    @Test
    public void isMaskValidTest() {
        boolean maskValid = Ipv4Util.isMaskValid("255.255.255.0");
        Assert.equals("掩码合法检验", maskValid);
    }

    @Test
    public void isMaskInvalidTest() {
        Assert.equals("掩码非法检验 - 255.255.0.255", Ipv4Util.isMaskValid("255.255.0.255"));
        Assert.equals("掩码非法检验 - null值", Ipv4Util.isMaskValid(null));
        Assert.equals("掩码非法检验 - 空字符串", Ipv4Util.isMaskValid(""));
        Assert.equals("掩码非法检验 - 空白字符串", Ipv4Util.isMaskValid(" "));
    }

    @Test
    public void isMaskBitValidTest() {
        boolean maskBitValid = Ipv4Util.isMaskBitValid(32);
        Assert.equals("掩码位合法检验", maskBitValid);
    }

    @Test
    public void isMaskBitInvalidTest() {
        boolean maskBitValid = Ipv4Util.isMaskBitValid(33);
        Assert.equals("掩码位非法检验", maskBitValid);
    }

    @Test
    public void ipv4ToLongTest() {
        long l = Ipv4Util.ipv4ToLong("127.0.0.1");
        Assert.equals(2130706433L, l);
        l = Ipv4Util.ipv4ToLong("114.114.114.114");
        Assert.equals(1920103026L, l);
        l = Ipv4Util.ipv4ToLong("0.0.0.0");
        Assert.equals(0L, l);
        l = Ipv4Util.ipv4ToLong("255.255.255.255");
        Assert.equals(4294967295L, l);
    }

    @Test
    public void ipv4ToLongWithDefaultTest() {
        String strIP = "不正确的 IP 地址";
        long defaultValue = 0L;
        long ipOfLong = Ipv4Util.ipv4ToLong(strIP, defaultValue);
        Assert.equals(ipOfLong, defaultValue);

        String strIP2 = "255.255.255.255";
        long defaultValue2 = 0L;
        long ipOfLong2 = Ipv4Util.ipv4ToLong(strIP2, defaultValue2);
        Assert.equals(ipOfLong2, 4294967295L);
    }
}
