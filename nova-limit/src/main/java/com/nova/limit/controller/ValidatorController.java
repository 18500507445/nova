package com.nova.limit.controller;

import com.nova.common.core.model.business.ValidatorReqDTO;
import com.nova.common.core.model.result.RespResult;
import com.nova.common.exception.base.ParamException;
import com.nova.common.utils.common.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/18 19:33
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class ValidatorController {

    /**
     * validator
     */
    @PostMapping("validator")
    public RespResult<Void> validator(@Validated @RequestBody ValidatorReqDTO reqDTO) {
        return RespResult.success();
    }

    /**
     * 自定义validator
     */
    @PostMapping("customValidator")
    public RespResult<Void> customValidator(ValidatorReqDTO reqDTO) {
        try {
            ValidatorUtil.validate(reqDTO);
        } catch (ParamException e) {
            return RespResult.error(e.getMessage());
        }
        return RespResult.success();
    }

}
