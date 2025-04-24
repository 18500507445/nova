package com.nova.common.utils.ip;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author: wzh
 * @description: 显示ip地点工具类
 * Ip2region简介，离线IP地址定位库和IP定位数据管理框架
 * @date: 2023/10/06 13:20
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegionUtils {

    public static final String UN_KNOW = "未知ip";

    //数据下载地址：https://github.com/lionsoul2014/ip2region/blob/master/data/ip2region.xdb
    public static BufferedInputStream INPUT_STREAM = FileUtil.getInputStream("ip2region.xdb");

    public static Searcher searcher;

    static {
        byte[] bytes = RegionUtils.inputStreamToByteArray(INPUT_STREAM);
        try {
            searcher = Searcher.newWithBuffer(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRegion(String ip) {
        String region = "";
        // jar包也能获取ip2region.xdb文件
        try {
            long sTime = System.nanoTime();
            // 中国|0|上海|上海市|联通；美国|0|犹他|盐湖城|0
            String regionInfo = searcher.search(ip);
            region = getCityInfo(regionInfo);
            long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sTime);
            System.err.printf("【IP属地 : %s, ip: %s, 耗时: %d 纳秒】\n", region, ip, cost);
        } catch (Exception e) {
            log.error("IP地址异常 {} : {}", ip, e);
        }
        return region;
    }

    /**
     * 将输入流转化为字节数组
     */
    public static byte[] inputStreamToByteArray(InputStream inputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int num;
            while ((num = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, num);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("inputStreamToByteArray异常", e);
        }
        return null;
    }

    /**
     * 解析城市信息，国内显示城市名，国外显示国家名
     */
    private static String getCityInfo(String regionInfo) {
        if (StrUtil.isNotBlank(regionInfo)) {
            String[] cityArr = regionInfo.replace("|0", "").replace("0|", "").split("\\|");
            if (cityArr.length > 0) {
                if ("内网ip".equalsIgnoreCase(cityArr[0])) {
                    return "内网IP";
                }
                if ("中国".equals(cityArr[0])) {
                    return cityArr[1];
                }
                return cityArr[0];
            }
        }
        return UN_KNOW;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            String region = getRegion("183.242.4.250");
            System.err.println("region = " + region);
        }
    }
}
