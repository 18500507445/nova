package com.nova.log.tlog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description TLog
 * @date: 2023/10/12 15:13
 */
@Slf4j(topic = "TLogController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class TLogController {

    @GetMapping("TLogTest")
    public void tLogTest() {
        log.error("测试TLog");
    }
}
