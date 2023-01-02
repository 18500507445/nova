package com.nova.tools.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.entity.RespResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: wzh
 * @date: 2022/11/19 17:19
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class DemoController extends BaseController {

    /**
     * demo
     */
    @PostMapping("demo")
    public RespResult<Object> demo(@RequestBody Object body) {
        return RespResult.success(body);
    }

}
