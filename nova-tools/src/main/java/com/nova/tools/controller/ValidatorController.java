package com.nova.tools.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.business.ValidatorReqDTO;
import com.nova.common.core.model.result.RespResult;
import com.nova.common.exception.base.ParamException;
import com.nova.common.trace.Trace;
import com.nova.common.utils.common.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/18 19:33
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class ValidatorController extends BaseController {

    /**
     * validator
     */
    @PostMapping("validator")
    public RespResult<String> validator(@Validated @RequestBody ValidatorReqDTO reqDTO) {
        HttpServletRequest request = getRequest();
        String header = request.getHeader(Trace.TRACE_ID);
        return RespResult.success(header);
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
