package com.nova.lock.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.AjaxResult;
import com.nova.common.core.domain.ValidatorReqDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/11/19 17:19
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class DemoController extends BaseController {

    /**
     * redisson
     */
    @PostMapping("redisson")
    @ResponseBody
    public AjaxResult redisson(ValidatorReqDto reqDto) {
        return AjaxResult.success(reqDto);
    }

}
