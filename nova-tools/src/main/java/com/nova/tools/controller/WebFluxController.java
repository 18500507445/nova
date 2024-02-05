package com.nova.tools.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.result.ResResult;
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
 * @description: webflux 优势：响应式异步非阻塞式，劣势：需要很多api兼容，项目里面需要大量进行替换。非高并发项目对性能没有极高的要求不建议进行替换
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
    public Mono<ResResult<String>> getOne() {
        return Mono.just(ResResult.success("create"));
    }

    /**
     * Flux返回List对象
     */
    @GetMapping("/getList")
    public Flux<ResResult<List<String>>> getList() {
        return Flux.just(ResResult.success(Arrays.asList("1", "2", "3")));
    }

}
