package com.nova.lock.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.business.ValidatorReqDTO;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.lock.annotation.Lock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * redisson
     */
    @Lock(value = "redisson", expireSeconds = 60)
    @PostMapping("redisson")
    public AjaxResult redisson(ValidatorReqDTO reqDto) {
        return AjaxResult.success(reqDto);
    }

}
