package com.nova.tools.product.base;

import cn.hutool.core.util.ObjectUtil;
import com.nova.tools.product.enums.ChannelEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wzh
 * @description 渠道工厂
 * @date: 2023/10/24 10:11
 */
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChannelFactory {

    /**
     * 渠道容器
     */
    private static final Map<ChannelEnum, AbstractChannelBase> CONTAINER = new ConcurrentHashMap<>();

    /**
     * 获取渠道
     */
    public AbstractChannelBase get(ChannelEnum channelEnum) {
        return CONTAINER.get(channelEnum);
    }

    /**
     * 子类获取
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractChannelBase> T get(ChannelEnum channelEnum, Class<T> clazz) {
        if (AbstractChannelBase.class.isAssignableFrom(clazz)) {
            return (T) CONTAINER.get(channelEnum);
        }
        return null;
    }

    /**
     * 渠道放入容器
     */
    public static <T extends AbstractChannelBase> void add(ChannelEnum channelEnum, T abstractChannelBase) {
        if (ObjectUtil.isAllNotEmpty(channelEnum, abstractChannelBase)) {
            CONTAINER.put(channelEnum, abstractChannelBase);
        }
    }

}
