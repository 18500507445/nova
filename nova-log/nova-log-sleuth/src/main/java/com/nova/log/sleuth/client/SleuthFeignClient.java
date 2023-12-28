package com.nova.log.sleuth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: wzh
 * @description
 * @date: 2023/12/24 19:30
 */
@FeignClient(name = "sleuth-remote", url = "http://localhost:8081/api")
public interface SleuthFeignClient {

    @GetMapping("/sleuthTest")
    void sleuthTest();

}
