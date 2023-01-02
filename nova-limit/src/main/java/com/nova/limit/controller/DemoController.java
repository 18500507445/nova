package com.nova.limit.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.entity.AjaxResult;
import com.nova.common.core.entity.ValidatorReqDTO;
import com.nova.common.utils.common.ValidatorUtil;
import com.nova.limit.annotation.AccessLimit;
import com.nova.limit.config.LimitConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

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

    private final ApplicationContext applicationContext;

    private final WebApplicationContext webApplicationContext;

    /**
     * 限流demo
     */
    @PostMapping("limit")
    @AccessLimit(seconds = 5, maxCount = 5)
    public AjaxResult limit(@RequestBody Object body) {
        System.out.println("applicationContext = " + applicationContext);
        System.out.println("webApplicationContext = " + webApplicationContext);

        Map<String, LimitConfig> beansOfType = applicationContext.getBeansOfType(LimitConfig.class);
        System.out.println("beansOfType = " + beansOfType);

        LimitConfig bean = applicationContext.getBean(LimitConfig.class);
        System.out.println("bean = " + bean);

        return AjaxResult.success(body);
    }

    /**
     * validator
     */
    @PostMapping("validator")
    public AjaxResult validator(@Validated ValidatorReqDTO reqDto) {
        return AjaxResult.success("成功");
    }

    /**
     * 自定义validator
     */
    @PostMapping("customValidator")
    public AjaxResult customValidator(ValidatorReqDTO reqDto) {
        ValidatorUtil.validate(reqDto);
        return AjaxResult.success("成功");
    }

}
