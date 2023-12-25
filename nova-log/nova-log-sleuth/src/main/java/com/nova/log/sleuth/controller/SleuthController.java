package com.nova.log.sleuth.controller;

import com.nova.log.sleuth.client.SleuthRemoteService;
import com.nova.log.sleuth.thread.ThreadWrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: wzh
 * @description Sleuth
 * @date: 2023/10/12 15:45
 */
@Slf4j(topic = "SleuthController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SleuthController {

    private final SleuthRemoteService sleuthRemoteService;

    private final Executor jdkExecutor;


    /**
     * 复制一个SleuthApplication端口8081，添加add-vm-options（-Dserver.port=8081），进行测试，
     * 发现http调用后traceId是不同的，所以还是后续手动维护TraceId吧，不要用cloud-sleuth依赖包
     */
    @GetMapping("/sleuthTest")
    public void sleuthTest() {
        log.error("sleuthTest");
    }

    /**
     * RestTemplate，默认不支持traceId透传，需要进行改造
     */
    @GetMapping("/demoA")
    public void demoA() {
        log.error("demoA");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange("http://127.0.0.1:8081/api/sleuthTest", HttpMethod.GET, null, String.class);
    }

    /**
     * OpenFeign，默认支持，traceId会进行透传
     */
    @GetMapping("/demoB")
    public void demoB() {
        log.error("demoB");
        sleuthRemoteService.sleuthTest();
    }

    /**
     * newThread，默认不支持，需要进行改造
     */
    @GetMapping("/demoC")
    public void demoC() {
        log.error("demoC");
        new Thread(sleuthRemoteService::sleuthTest).start();
    }

    /**
     * 手动线程池，默认不支持，需要进行改造
     */
    @GetMapping("/demoD")
    public void demoD() {
        log.error("demoD");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20));
        threadPoolExecutor.setThreadFactory(new CustomizableThreadFactory("ThreadPoolExecutor"));
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutor.submit(sleuthRemoteService::sleuthTest);
    }

    /**
     * 通过配置类，注册bean的jdk线程池，默认支持，不需要改造
     */
    @GetMapping("/demoE")
    public void demoE() {
        log.error("demoE");
        jdkExecutor.execute(sleuthRemoteService::sleuthTest);
    }

    @GetMapping("/demoF")
    public void newThreadSpring() {
        log.error("demoE");
        async();
    }

    /**
     * 通过配置类，注册bean的spring带的线程池，默认支持，不需要改造
     */
    @Async("springExecutor")
    public void async() {
        log.error("async");
        sleuthRemoteService.sleuthTest();
    }

    @GetMapping("/demoG")
    public void demoG() {
        log.error("demoG");
        log.warn("main-threadId = {}", Thread.currentThread().getId());
        new Thread(() -> log.error("demoG-thread-noWrap")).start();
        new Thread(ThreadWrap.runnableWrap(() -> log.error("demoG-thread"))).start();
    }

    @GetMapping("/demoH")
    public void demoH() {
        log.error("demoH");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20));
        threadPoolExecutor.setThreadFactory(new CustomizableThreadFactory("ThreadPoolExecutor"));
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        threadPoolExecutor.submit(() -> log.error("demoH-thread-noWrap"));
        threadPoolExecutor.submit(ThreadWrap.runnableWrap(() -> log.error("demoH-thread")));
    }

}
