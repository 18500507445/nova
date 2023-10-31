package com.nova.tools.product.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: wzh
 * @description 渠道枚举，因code值多个项目之间定义有不同防止串值，索性用拼音缩写代替
 * @date: 2023/10/24 10:15
 */
@Getter
@AllArgsConstructor
public enum ChannelEnum {

    JD("jd", "京东"),

    AFK("xf", "自营-小福"),

    YX("yx", "网易-严选"),

    ;

    private final String name;

    private final String description;

    /**
     * 获取渠道枚举
     */
    public static ChannelEnum getChannel(String name) {
        return EnumUtil.getBy(ChannelEnum::getName, name, JD);
    }
}
