package com.nova.common.exception;

import com.nova.common.core.model.result.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 参数校验异常
 *
 * @date:2022/6/23 21:15
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ParamException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ResultCode resultCode;

    public ParamException(String message) {
        super(message);
    }

    public ParamException(int code, String message) {
        super(message);
    }

    public ParamException(ResultCode resultCode) {
        super(resultCode.getBizMessage());
        this.resultCode = resultCode;
    }

    public ParamException(Throwable cause) {
        super(cause);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
