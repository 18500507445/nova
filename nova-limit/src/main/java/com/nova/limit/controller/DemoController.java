package com.nova.limit.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.AjaxResult;
import com.nova.common.core.domain.ValidatorReqDto;
import com.nova.common.utils.common.ValidatorUtil;
import com.nova.limit.annotation.AccessLimit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

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

    private final ApplicationContext applicationContext;

    private final WebApplicationContext webApplicationContext;

    /**
     * 限流demo
     */
    @PostMapping("limit")
    @ResponseBody
    @AccessLimit(seconds = 5, maxCount = 5)
    public AjaxResult limit(@RequestBody Object body) {
        return AjaxResult.success(body);
    }

    /**
     * validator
     */
    @PostMapping("validator")
    @ResponseBody
    public AjaxResult validator(@Validated ValidatorReqDto reqDto) {
        return AjaxResult.success("成功");
    }

    /**
     * 自定义validator
     */
    @PostMapping("customValidator")
    @ResponseBody
    public AjaxResult customValidator(ValidatorReqDto reqDto) {
        ValidatorUtil.validate(reqDto);
        return AjaxResult.success("成功");
    }
}
