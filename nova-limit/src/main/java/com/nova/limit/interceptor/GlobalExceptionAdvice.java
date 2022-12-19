package com.nova.limit.interceptor;

import com.nova.common.core.domain.AjaxResult;
import com.nova.common.exception.base.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @Description: 全局异常拦截器
 * @Author: wangzehui
 * @Date: 2022/12/19 20:47
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 拦截的validator异常
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e) {
        log.debug("handleBindException：{}", e.getMessage());

        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String message = fieldError.getDefaultMessage();
            msg.append(message);
        }
        return AjaxResult.error("1000", msg.toString());
    }

    /**
     * 自定义参数异常
     * @param e
     * @return
     */
    @ExceptionHandler(ParamException.class)
    public AjaxResult paramException(ParamException e) {
        log.debug("paramException :{}", e.getMessage(), e);
        return AjaxResult.error("1000", e.getMessage());
    }
}
