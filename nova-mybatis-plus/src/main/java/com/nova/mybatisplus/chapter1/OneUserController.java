package com.nova.mybatisplus.chapter1;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.nova.common.core.model.result.AjaxResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description userController
 * @date: 2023/06/15 19:56
 */
@Slf4j(topic = "UserController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class OneUserController {

    private final OneUserService oneUserService;

    private static final TimeInterval TIMER = DateUtil.timer();

    @PostMapping("selectList")
    public AjaxResult selectList() {
        try {
            return AjaxResult.success(oneUserService.selectList());
        } finally {
            log.info("接口：selectList，耗时：{} ms", TIMER.interval());
        }
    }
}