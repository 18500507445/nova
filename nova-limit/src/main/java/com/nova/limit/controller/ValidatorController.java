package com.nova.limit.controller;

import com.nova.common.core.model.business.ValidatorReqDTO;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.common.utils.common.ValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/18 19:33
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class ValidatorController {

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
