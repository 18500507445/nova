package com.nova.limit.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.AjaxResult;
import com.nova.limit.annotation.AccessLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/11/19 17:19
 */
@Slf4j
@RestController
@RequestMapping("/api/")
public class DemoController extends BaseController {

    /**
     * demoA
     */
    @PostMapping("demoA")
    @ResponseBody
    @AccessLimit(seconds = 5, maxCount = 5)
    public AjaxResult demoA(@RequestBody Object body) {
        return AjaxResult.success(body);
    }
}
