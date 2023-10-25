package com.nova.tools.product;

import cn.hutool.core.util.EnumUtil;
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
        return EnumUtil.getBy(ChannelEnum::getChannelId, channelId, JD);
    }
}
