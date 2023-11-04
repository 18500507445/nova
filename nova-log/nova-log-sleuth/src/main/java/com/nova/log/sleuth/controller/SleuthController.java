package com.nova.log.sleuth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("sleuthTest")
    public void sleuthTest() {
        log.info("测试spring-cloud-sleuth");
    }
}
