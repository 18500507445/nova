package com.nova.tools.demo;

import com.starter.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description 测试Controller
 * @date: 2023/05/26 12:09
 */
@Slf4j(topic = "DemoController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class DemoController {

    private final RedisService redisService;

}
