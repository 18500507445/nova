package com.nova.common.utils.spring;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: wzh
 * @description spring属性工具类
 * @date: 2023/07/24 13:25
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesUtils {

    /**
     * 属性文件与属性集合的关系
     */
    private final Map<String, Properties> propertiesMap = new HashMap<>();

    /**
     * 属性文件与修改时间的关系
     */
    private final Map<String, Long> modifyTimeMap = new HashMap<>();

    /**
     * 指定的属性文件的目录路径
     */
    private String configPath = "";

    private static class SingleHolder {
        private static final PropertiesUtils instance = new PropertiesUtils();
    }

    public static PropertiesUtils getInstance() {
        return SingleHolder.instance;
    }

    public void configure(String path) {
        this.configPath = path;
    }

    /**
     * 获取指定属性的值
     */
    public String getPropertyValue(String propertyFileName, String key) {
        String fileName = convertPropertiesFileName(propertyFileName);
        try {
            if (propertiesMap.get(fileName) == null) {
                loadProperties(fileName);
            } else {
                checkPropertiesFileModified(fileName);
            }
            return propertiesMap.get(fileName).getProperty(key);
        } catch (Exception e) {
            log.error("PropertiesUtils.getPropertyValue.error:{}", e.getMessage(), e);
        }
        return "";
    }

    /**
     * 对传入的属性文件名称进行处理，如果包含.properties后缀则去掉
     */
    private String convertPropertiesFileName(String propertyFileName) {
        String fileName = propertyFileName;
        if (fileName.endsWith(".properties")) {
            int index = fileName.lastIndexOf(".");
            fileName = fileName.substring(0, index);
        }
        return fileName;
    }

    /**
     * 加载指定名称的属性文件
     */
    private void loadProperties(String shortPropertyFileName) throws URISyntaxException {
        File file = getPropertiesFile(shortPropertyFileName);
        Long newTime = file.lastModified();
        if (propertiesMap.get(shortPropertyFileName) != null) {
            propertiesMap.remove(shortPropertyFileName);
        }
        Properties props = new Properties();
        try {
            props.load(Files.newInputStream(file.toPath()));
        } catch (Exception e) {
            log.error("PropertiesUtils.loadProperties.error:{}", e.getMessage(), e);
        }
        propertiesMap.put(shortPropertyFileName, props);
        modifyTimeMap.put(shortPropertyFileName, newTime);
    }

    /**
     * 检查属性文件有无更新，若更新则重新加载
     */
    private void checkPropertiesFileModified(String shortPropertyFileName) throws URISyntaxException {
        File file = getPropertiesFile(shortPropertyFileName);
        long newTime = file.lastModified();
        Long lastModifiedTime = modifyTimeMap.get(shortPropertyFileName);
        if (newTime == 0) {
            if (lastModifiedTime == null) {
                log.error(shortPropertyFileName + ".properties file does not exist!");
            }
        } else if (newTime > lastModifiedTime) {
            loadProperties(shortPropertyFileName);
        }
    }

    /**
     * 获取属性文件的去路径，首先
     */
    private File getPropertiesFile(String shortPropertyFileName) throws URISyntaxException {
        File propertiesFile;
        if (this.configPath != null && !"".equals(this.configPath.trim())) {
            return new File(this.configPath + File.separator + shortPropertyFileName + ".properties");
        }
        String dir = System.getProperty("user.dir") + File.separator + shortPropertyFileName + ".properties";
        propertiesFile = new File(dir);
        if (!propertiesFile.exists()) {
            URL url = PropertiesUtils.class.getResource("/" + shortPropertyFileName + ".properties");
            if (url == null) {
                propertiesFile = null;
            } else {
                propertiesFile = new File(url.toURI());
            }
        }
        return propertiesFile;
    }
}
