package com.nova.tools.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: wzh
 * @description 渠道枚举
 * @date: 2023/10/24 10:15
 */
@Getter
@AllArgsConstructor
public enum ChannelEnum {

    DEFAULT(0, "默认"),

    JD(1, "京东"),

    AFK(2, "自营-小福"),

    YX(3, "网易-严选"),

    ;

    private final int channelId;

    private final String channelName;

    /**
     * 获取渠道枚举
     */
    public static ChannelEnum getChannel(int channelId) {
        switch (channelId) {
            case 1:
                return JD;
            case 2:
                return AFK;
            case 3:
                return YX;
            default:
                return DEFAULT;
        }
    }
}
