package com.nova.pay.untils;

import com.nova.pay.config.WeChatConfig;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Component;

/**
 * @Description: 微信公众号工具类
 * @Author: wangzehui
 * @Date: 2022/8/5 09:10
 */
@Component
public class WeChatMpUtils {

    public static final WxMpService wxMpService = WeChatConfig.getWxMpService();


}
