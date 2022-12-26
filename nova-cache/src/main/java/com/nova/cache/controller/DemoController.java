package com.nova.cache.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.RespResult;
import com.nova.common.core.domain.ValidatorReqDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description: 测试demo
 * @Author: wangzehui
 * @Date: 2022/11/19 17:19
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
    public RespResult<ValidatorReqDto> redisson(@Validated ValidatorReqDto reqDto) {
        return RespResult.success(reqDto);
    }


}
