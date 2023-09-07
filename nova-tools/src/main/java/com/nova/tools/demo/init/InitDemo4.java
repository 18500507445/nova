package com.nova.tools.demo.init;

import org.springframework.boot.SpringApplication;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/11 10:40
 */
//@SpringBootApplication
class InitDemo4 {

    /**
     * 最后一种方式，Application main方法里初始化
     * @param args
     */
    static void main(String[] args) {
        SpringApplication.run(InitDemo4.class, args);
        System.err.println("[main] 初始化");
    }

}
