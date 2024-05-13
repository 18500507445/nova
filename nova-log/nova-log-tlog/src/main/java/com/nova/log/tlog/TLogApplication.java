package com.nova.log.tlog;

import com.yomahub.tlog.core.enhance.bytes.AspectLogEnhance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 * @description TLog启动类
 * @date: 2023/10/12 15:12
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TLogApplication {

    public static void main(String[] args) {
        //进行日志增强，自动判断日志框架
        AspectLogEnhance.enhance();
        System.err.println("异步输出log，注意看下logback-spring.xml，搜索一下com.yomahub.tlog看下与正常配置的logback不同的地方，详情见官方文档");
        SpringApplication.run(TLogApplication.class, args);
    }

}
