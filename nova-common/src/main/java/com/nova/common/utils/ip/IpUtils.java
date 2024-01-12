package com.nova.common.utils.ip;

import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;


/**
 * @author wzh
 */
@Slf4j(topic = "IpUtils")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IpUtils {

    private static final String UNKNOWN = "未知ip";

    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            // k8s将真实的客户端IP，放到了x-Original-Forwarded-For。而将WAF的回源地址放到了 x-Forwarded-For了。
            ip = request.getHeader("X-Original-Forwarded-For");
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }
            // 通过nginx获取ip
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-forwarded-for");
            }
            // 通过Apache代理获取ip
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            // 通过WebLogic代理获取ip
            if (StrUtil.isEmpty(ip) || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            // 通过负载均衡获取IP地址（HTTP_CLIENT_IP、HTTP_X_FORWARDED_FOR）
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            // 通过Nginx获取ip（Nginx中的另一个变量，内容就是请求中X-Forwarded-For的信息）
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            //兼容集群获取ip
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                // 客户端和服务器为同一台机器时，获取的地址为IPV6格式："0:0:0:0:0:0:0:1"
                if ("127.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
                    //根据网卡取本机配置的IP
                    InetAddress iNet;
                    try {
                        iNet = InetAddress.getLocalHost();
                        ip = iNet.getHostAddress();
                    } catch (UnknownHostException e) {
                        log.error("根据网卡获取IP地址异常: ", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取IP地址异常 ", e);
        }
        //使用代理，则获取第一个IP地址
        if (ip != null && StrUtil.isNotBlank(ip) && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {

        }
        return "127.0.0.1";
    }

    /**
     * MacBook-Pro.local
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {

        }
        return "未知";
    }

    /**
     * 获取mac地址，6C-7E-67-C4-B9-0F
     */
    public static String getMacAddress() throws Exception {
        // 取mac地址
        byte[] macAddressBytes = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        // 下面代码是把mac地址拼装成String
        StringBuilder sb = new StringBuilder();
        if (macAddressBytes.length > 0) {
            for (int i = 0; i < macAddressBytes.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(macAddressBytes[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
        }
        return sb.toString().trim().toUpperCase();
    }


}