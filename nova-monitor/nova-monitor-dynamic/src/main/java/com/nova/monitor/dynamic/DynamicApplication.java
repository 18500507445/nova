package com.nova.monitor.dynamic;

import org.dromara.dynamictp.core.spring.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: wzh
 * @description 动态线程池监控
 * @date: 2023/10/07 17:19
 */
@SpringBootApplication
@EnableDynamicTp
public class DynamicApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicApplication.class, args);
    }

}
