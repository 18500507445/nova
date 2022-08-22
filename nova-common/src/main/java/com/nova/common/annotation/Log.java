package com.nova.common.annotation;


import com.nova.common.enums.BusinessType;
import com.nova.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * @author wangzehuit
 * @Title: Log
 * @ProjectName crazy
 * @Description: 自定义操作日志记录注解
 * @date 2021/7/18 16:18
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;
}

