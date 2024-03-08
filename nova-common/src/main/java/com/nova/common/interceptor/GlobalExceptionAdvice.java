package com.nova.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.nova.common.core.model.result.ResResult;
import com.nova.common.core.model.result.ResultCode;
import com.nova.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @description: 全局异常拦截器
 * @author: wzh
 * @date: 2022/12/19 20:47
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 拦截的validator异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResResult<Void> handleBindException(BindException e) {
        log.warn("handleBindException：{}", e.getMessage());
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String message = fieldError.getDefaultMessage();
            msg.append(message).append("，");
        }
        return ResResult.failure(ResultCode.VALIDATE_FAILED, StrUtil.subWithLength(msg.toString(), 0, msg.length() - 1));
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResResult<Void> businessException(BusinessException e) {
        log.error("businessException :{}", e.getMessage(), e);
        return ResResult.failure(ResultCode.FAILED, e.getMessage());
    }
}
