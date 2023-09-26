package com.nova.common.utils.ip;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取IP方法
 */
@Slf4j(topic = "IpUtils")
public class IpUtils {

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("client-ip");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
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
     * 获取ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 本机访问
        if ("localhost".equalsIgnoreCase(ip) || "127.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            // 根据网卡取本机配置的IP
            InetAddress inet;
            try {
                inet = InetAddress.getLocalHost();
                ip = inet.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (null != ip && ip.length() > 15) {
            if (ip.indexOf(",") > 15) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
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

    /**
     * 太墨迹，耗时很久啊
     *
     * @return
     * @throws Exception
     */
    public static String getInternetIpTwo() throws Exception {
        String ip = null;
        String china = "https://ip.chinaz.com";
        StringBuilder inputLine = new StringBuilder();
        String read;
        URL url;
        HttpURLConnection urlConnection;
        BufferedReader in = null;
        try {
            url = new URL(china);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            while ((read = in.readLine()) != null) {
                inputLine.append(read).append("\r\n");
            }
            Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
            Matcher m = p.matcher(inputLine.toString());
            if (m.find()) {
                ip = m.group(1);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        if (ip == null) {
            throw new RuntimeException();
        }
        return ip;
    }

    public static void main(String[] args) throws Exception {
        String internetIp = getInternetIp("curl cip.cc");
        System.out.println(internetIp);
    }

    public static String getInternetIp(String command) {
        TimeInterval timer = DateUtil.timer();
        String ip = "";
        String result = "";
        String line;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);
            Process process = processBuilder.start();
            // 获取命令输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                if (StrUtil.containsAnyIgnoreCase(line, "ip", ":")) {
                    List<String> split = StrUtil.split(line, ":");
                    if (split.size() > 1) {
                        ip = split.get(1);
                    }
                    break;
                }
            }
            List<String> split = StrUtil.split(ip, ".");
            if (split.size() > 3) {
                result = split.get(3);
            }
            log.info("getInternetIp--ip：{}，result：{}，耗时：{} ms", ip, result, timer.interval());
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 执行Linux命令，curl cip.cc
     *
     * @param command
     */
    public static void executeLinux(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);
            Process process = processBuilder.start();
            // 获取命令输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 获取命令错误输出
            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
            // 等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("命令执行完成，退出码：" + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}