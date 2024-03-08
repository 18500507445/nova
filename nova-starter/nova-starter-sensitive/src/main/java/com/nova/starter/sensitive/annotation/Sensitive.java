package com.nova.starter.sensitive.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nova.starter.sensitive.SensitiveSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: wzh
 * @description: Sensitive
 * @date: 2024/03/08 13:24
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerializer.class)
public @interface Sensitive {

    /**
     * 脱敏类型，默认手机号
     */
    Type value();

    /**
     * CUSTOM_HIDE、CUSTOM_OVERLAY 时生效
     * 下标开始位置（包含）
     */
    int start() default 0;

    /**
     * CUSTOM_HIDE、CUSTOM_OVERLAY 时生效
     * 下标结束位置（不包含）
     */
    int end() default 0;

    /**
     * CUSTOM_OVERLAY 时生效,'*'重复的次数
     */
    int overlay() default 4;

    /**
     * Enumeration used with {@link Sensitive}
     */
    enum Type {

        //手机号
        MOBILE,

        //中文名
        CHINESE_NAME,

        //身份证号
        ID_CARD,

        //座机号
        FIXED_PHONE,

        //地址
        ADDRESS,

        //电子邮件
        EMAIL,

        //银行卡
        BANK_CARD,

        //自定义，[start,end)之间的字符都替换成 '*'
        CUSTOM_HIDE,

        //自定义，保留前start个、后end个字符，其余的都替换成 '*'
        CUSTOM_RETAIN_HIDE,

        //自定义，只替换成指定个数的'*'
        CUSTOM_OVERLAY,
    }

}
