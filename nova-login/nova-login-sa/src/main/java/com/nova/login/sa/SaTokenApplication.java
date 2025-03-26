package com.nova.login.sa;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @description:
 * @author: wzh
 * @date: 2022/9/8 19:43
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SaTokenApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaTokenApplication.class, args);
        System.err.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
        System.err.println("官网：https://sa-token.cc/doc.html#/");
    }

}
