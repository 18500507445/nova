package com.nova.monitor.sba;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 添加开启SBA服务端后台注解
 * @author wzh
 */
@EnableAdminServer
@SpringBootApplication
public class SbaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbaApplication.class, args);
    }

}
