package com.nova.tools.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.result.RespResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @author: wzh
 * @description: webflux
 * @date: 2024/01/26 10:41
 */
@Slf4j(topic = "WebFluxController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/webflux")
public class WebFluxController extends BaseController {

    /**
     * Mono返回单个对象
     */
    @GetMapping("/getOne")
    public Mono<RespResult<String>> getOne() {
        return Mono.just(RespResult.success("create"));
    }

    /**
     * Flux返回List对象
     */
    @GetMapping("/getList")
    public Flux<RespResult<List<String>>> getList() {
        return Flux.just(RespResult.success(Arrays.asList("1", "2", "3")));
    }

}
