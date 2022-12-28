package com.nova.pay.payment.open;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Component;

/**
 * @description: 微信配置
 * @author: wangzehui
 * @date: 2022/8/19 10:19
 */
@Component
public class WeChatPayment {

    /**
     * 微信v2配置
     *
     * @param appId   应用id
     * @param mchId   商户id
     * @param mchKey  商户秘钥
     * @param keyPath apiclient_cert.p12
     * @return
     */
    public WxPayService getWxV2PayService(String appId, String mchId, String mchKey, String keyPath) {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setMchKey(mchKey);
        payConfig.setKeyPath(keyPath);
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 微信V3配置
     *
     * @param appId           应用id
     * @param mchId           商户id
     * @param apiV3Key        商户支付v3key
     * @param serialNo        证书号
     * @param privateKeyPath  apiclient_key.pem
     * @param privateCertPath apiclient_cert.pem
     * @return
     */
    public WxPayService getWxV3PayService(String appId, String mchId, String apiV3Key, String serialNo, String privateKeyPath, String privateCertPath) {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setApiV3Key(apiV3Key);
        payConfig.setCertSerialNo(serialNo);
        payConfig.setPrivateKeyPath(privateKeyPath);
        payConfig.setPrivateCertPath(privateCertPath);
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 微信公众号配置
     *
     * @param appId  应用id
     * @param secret 应用秘钥
     * @return
     */
    public WxMpService getWxMpService(String appId, String secret) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setAppId(appId);
        configStorage.setSecret(secret);
        wxMpService.setWxMpConfigStorage(configStorage);
        return wxMpService;
    }

}
