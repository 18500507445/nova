package com.nova.tools.demo;

import cn.hutool.core.date.DateTime;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.tools.demo.entity.People;
import com.starter.redis.RedisService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description 测试Controller
 * @date: 2023/05/26 12:09
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class DemoController {

    private final RedisService redisService;

    @PostMapping ("setList")
    public AjaxResult setList(@RequestParam String name) {
        for (int i = 1; i < 3; i++) {
            People build = People.builder().id(i).age(1).createTime(new DateTime()).build();
            redisService.setList(name, build);
        }
        return AjaxResult.success("success");
    }

    @PostMapping("getList")
    public AjaxResult getList(@RequestParam String name) {
        return AjaxResult.success(redisService.listPop(name));
    }
}
