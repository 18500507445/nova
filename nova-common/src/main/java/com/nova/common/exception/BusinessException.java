package com.nova.common.exception;

import com.nova.common.core.model.result.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author: wzh
 * @description 业务异常
 * @date: 2024/01/03 19:41
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ResultCode resultCode;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getBizMessage());
        this.resultCode = resultCode;
    }

    public BusinessException(int code, String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
