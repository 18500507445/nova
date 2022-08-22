package com.nova.pay.utils;

import com.nova.pay.config.WeChatConfig;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

/**
 * @Description: 微信公众号工具类
 * @Author: wangzehui
 * @Date: 2022/8/5 09:10
 */
@Component
public class WeChatMpUtils {

    public static final WxMpService wxMpService = WeChatConfig.getWxMpService();

    public static void main(String[] args) {
        WeChatMpUtils weChatMpUtils = new WeChatMpUtils();
        System.out.println(weChatMpUtils.getUserInfo("o7vUD6ebtkEualtIMNXpDS6ixIDE","zh_CN"));
    }

    /**
     * 用code换取oauth2的openid
     * 详情请见: http://mp.weixin.qq.com/wiki/1/8a5ce6257f1d3b2afb20f83e72b72ce9.html
     *
     * @param authCode
     */
    public String getOpenid(String authCode) {
        String openId = "";
        try {
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(authCode);
            if (null != accessToken) {
                openId = accessToken.getOpenId();
            }
        } catch (WxErrorException e) {

        }
        return openId;
    }


    /**
     * 通过openid获得基本用户信息
     * 详情请见: http://mp.weixin.qq.com/wiki/14/bb5031008f1494a59c6f71fa0f319c66.html
     *
     * @param openid openid
     * @param lang   zh_CN, zh_TW, en
     */
    public WxMpUser getUserInfo(String openid, String lang) {
        WxMpUser wxMpUser = null;
        try {
            wxMpUser = wxMpService.getUserService().userInfo(openid, lang);

        } catch (WxErrorException e) {

        }
        return wxMpUser;
    }

    /**
     * 通过code获得基本用户信息
     * 详情请见: http://mp.weixin.qq.com/wiki/14/bb5031008f1494a59c6f71fa0f319c66.html
     *
     * @param code code
     * @param lang zh_CN, zh_TW, en
     */
    public WxMpUser getOAuth2UserInfo(String code, String lang) {
        WxMpUser wxMpUser = null;
        try {
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            wxMpUser = wxMpService.getUserService().userInfo(accessToken.getOpenId(), lang);
        } catch (WxErrorException e) {

        }
        return wxMpUser;
    }

}
