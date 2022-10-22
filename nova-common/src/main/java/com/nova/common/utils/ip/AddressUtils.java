package com.nova.common.utils.ip;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.common.config.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 获取地址类
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    /**
     * IP地址查询
     */
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    /**
     * 未知地址
     */
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (Global.isAddressEnabled()) {
            try {
                String rspStr = HttpUtil.createGet(IP_URL)
                        .form("ip", ip)
                        .form("json", "ture")
                        .execute()
                        .body();
                if (ObjectUtil.isEmpty(rspStr)) {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSONObject.parseObject(rspStr);
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s", region, city);
            } catch (Exception e) {
                log.error("获取地理位置异常 {}", e.getMessage());
            }
        }
        return UNKNOWN;
    }

    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("getIpAddress====>获取本地IP失败:{}", e);
        }
        return "";
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(getRealAddressByIP("223.221.240.148"));
        }
    }
}
