package com.nova.tools.demo.springboot.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description CommandLineRunner
 * 接口也只有一个方法：run(String... args)，触发时机为整个项目启动完毕后，自动执行。如果有多个CommandLineRunner，可以利用@Order来进行排序。
 * @date: 2023/10/08 17:35
 */
@Component
public class TestCommandLineRunner implements CommandLineRunner {

    /**
     * 使用场景：用户扩展此接口，进行启动项目之后一些业务的预处理
     */
    @Override
    public void run(String... args) throws Exception {
        System.err.println("[实现CommandLineRunner] 初始化");
    }
}
