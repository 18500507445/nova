package com.nova.pay.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Component;

/**
 * @Description: 微信配置
 * @Author: wangzehui
 * @Date: 2022/8/4 21:29
 */
@Component
public class WeChatConfig {

    /**
     * 设置微信配置
     *
     * @return
     */
    public static WxPayService getWxPayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId("");
        payConfig.setMchId("");
        payConfig.setMchKey("");
        payConfig.setKeyPath("");
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 设置企业微信配置
     *
     * @return
     */
    public static WxPayService getEntWxPayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId("");
        payConfig.setMchId("");
        payConfig.setMchKey("");
        payConfig.setKeyPath("");
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 设置微信公众号配置
     *
     * @return
     */
    public static WxMpService getWxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setAppId("");
        configStorage.setSecret("");
        configStorage.setToken("");
        configStorage.setAesKey("");
        wxMpService.setWxMpConfigStorage(configStorage);
        return wxMpService;
    }
}
