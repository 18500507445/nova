package com.nova.log.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.AjaxResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/11/19 17:19
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class DemoController extends BaseController {

    /**
     * log
     */
    @PostMapping("log")
    @ResponseBody
    public AjaxResult limit() {
        return AjaxResult.success("ok");
    }

}
