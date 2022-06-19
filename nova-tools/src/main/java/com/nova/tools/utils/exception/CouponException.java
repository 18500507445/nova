package com.nova.tools.utils.exception;

import lombok.Data;

/**
 * 票券模块自定义异常
 *
 * @author wuya
 */
@Data
public class CouponException extends RuntimeException {

    private static final long serialVersionUID = 6402987655616847030L;

    private static final String DEFAULT_ERROR_CODE = "9999";

    /**
     * 自定义错误编码
     */
    private String code;

    /**
     * 自定义错误信息
     */
    private String msg;

    public CouponException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public CouponException() {
        super();
    }

    public CouponException(Exception e) {
        super(e);
    }

    public CouponException(String code, String msg, Exception e) {
        super(e);
        this.code = code;
        this.msg = msg;
    }

    public CouponException(String msg) {
        super();
        this.code = DEFAULT_ERROR_CODE;
        this.msg = msg;
    }


}
