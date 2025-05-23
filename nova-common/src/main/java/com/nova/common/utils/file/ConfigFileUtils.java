package com.nova.common.utils.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: wzh
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigFileUtils {

    @SuppressWarnings("unchecked")
    public static Map<String, String> resourcePropertyFileToMap(String resourcePropertyFileName) {
        Properties properties = new Properties();
        InputStream is;
        try {
            is = ConfigFileUtils.class.getClassLoader().getResourceAsStream(resourcePropertyFileName);
            properties.load(is);
        } catch (Exception e) {
            System.err.println("不能读取配置文件 ：" + resourcePropertyFileName);
        }
        return new LinkedHashMap<String, String>((Map) properties);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> propertyFileInputStreamToMap(InputStream propertyFileInputStream) {
        Properties properties = new Properties();
        try {
            properties.load(propertyFileInputStream);
        } catch (Exception e) {
            System.err.println("不能读取配置文件");
        }
        return new LinkedHashMap<String, String>((Map) properties);
    }

}
