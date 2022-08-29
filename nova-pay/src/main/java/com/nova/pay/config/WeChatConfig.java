package com.nova.pay.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
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
     * 设置微信V2配置
     *
     * @return
     */
    public static WxPayService getWxV2PayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId("wx35252cfa011412e8");
        payConfig.setMchId("1625017129");
        payConfig.setMchKey("365Fengkuang365Fengkuang36536212");
        payConfig.setKeyPath("/Users/wangzehui/IdeaProjects/nova/nova-pay/src/main/resources/cert/1625017129/apiclient_cert.p12");
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 设置微信V3配置
     *
     * @return
     */
    public static WxPayService getWxV3PayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId("wxae6abcf7c685e716");
        payConfig.setMchId("1625017129");
        payConfig.setApiV3Key("365Fengkuang365Fengkuang36536212");
        payConfig.setCertSerialNo("5F1F1A03BB37A9B39C70A0AB5684CCEDA6E2E4EA");
        payConfig.setPrivateKeyPath("/Users/wangzehui/IdeaProjects/nova/nova-pay/src/main/resources/cert/1625017129/apiclient_key.pem");
        payConfig.setPrivateCertPath("/Users/wangzehui/IdeaProjects/nova/nova-pay/src/main/resources/cert/1625017129/apiclient_cert.pem");
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
        WxPayConfig entWxPayConfig = new WxPayConfig();
        entWxPayConfig.setAppId("");
        entWxPayConfig.setMchId("");
        entWxPayConfig.setMchKey("");
        entWxPayConfig.setKeyPath("");
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(entWxPayConfig);
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
        configStorage.setAppId("wxae6abcf7c685e716");
        configStorage.setSecret("ada94b3aeb0996ab8e990d87cab33634");
        wxMpService.setWxMpConfigStorage(configStorage);
        return wxMpService;
    }
}
