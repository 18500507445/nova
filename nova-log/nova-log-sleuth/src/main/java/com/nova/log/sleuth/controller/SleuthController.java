package com.nova.log.sleuth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author: wzh
 * @description Sleuth
 * @date: 2023/10/12 15:45
 */
@Slf4j(topic = "SleuthController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class SleuthController {


    /**
     * 复制一个SleuthApplication端口8081，添加add-vm-options（-Dserver.port=8081），进行测试，
     * 发现http调用后traceId是不同的，所以还是后续手动维护TraceId吧，不要用cloud-sleuth依赖包
     */
    @GetMapping("sleuthTest")
    public void sleuthTest() {
        RestTemplate restTemplate = new RestTemplate();
        log.info("测试spring-cloud-sleuth");
        restTemplate.exchange("http://127.0.0.1:8081/api/sleuthTest", HttpMethod.GET, null, String.class);
    }
}
