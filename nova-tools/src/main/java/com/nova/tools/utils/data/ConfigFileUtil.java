package com.nova.tools.utils.data;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 2019/9/11
 */
public class ConfigFileUtil {
    public static Map<String, String> resourcePropertyFileToMap(String resourcePropertyFileName) {
        Properties properties = new Properties();
        InputStream is = null;
        try {
            is = ConfigFileUtil.class.getClassLoader().getResourceAsStream(resourcePropertyFileName);
            properties.load(is);
        } catch (Exception e) {
            System.out.println("不能读取配置文件 ：" + resourcePropertyFileName);
        }
        return new LinkedHashMap<String, String>((Map) properties);
    }

    public static Map<String, String> propertyFileInputStreamToMap(InputStream propertyFileInputStream) {
        Properties properties = new Properties();
        try {
            properties.load(propertyFileInputStream);
        } catch (Exception e) {
            System.out.println("不能读取配置文件");
        }
        return new LinkedHashMap<String, String>((Map) properties);
    }

}
