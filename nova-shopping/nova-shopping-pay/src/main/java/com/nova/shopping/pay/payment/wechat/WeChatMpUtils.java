package com.nova.shopping.pay.payment.wechat;

import com.mall.pay.payment.open.WeChatPayment;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 微信公众号工具类
 * @author: wzh
 * @date: 2023/3/14 22:17
 */
@Component
public class WeChatMpUtils {

    @Resource
    private WeChatPayment weChatPayment;

    public static void main(String[] args) {
        WeChatMpUtils weChatMpUtils = new WeChatMpUtils();
        System.out.println(weChatMpUtils.getUserInfo("o7vUD6ebtkEualtIMNXpDS6ixIDE", "zh_CN"));
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
            WxOAuth2AccessToken accessToken = weChatPayment.getWxMpService().getOAuth2Service().getAccessToken(authCode);
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
            wxMpUser = weChatPayment.getWxMpService().getUserService().userInfo(openid, lang);

        } catch (WxErrorException e) {

        }
        return wxMpUser;
    }

    /**
     * 用oauth2获取用户信息, 当前面引导授权时的scope是snsapi_userinfo的时候才可以.
     *
     * @param code code
     * @param lang zh_CN, zh_TW, en
     */
    public WxOAuth2UserInfo getOAuth2UserInfo(String code, String lang) {
        WxOAuth2UserInfo userInfo = null;
        try {
            WxOAuth2AccessToken accessToken = weChatPayment.getWxMpService().getOAuth2Service().getAccessToken(code);
            userInfo = weChatPayment.getWxMpService().getOAuth2Service().getUserInfo(accessToken, lang);
        } catch (WxErrorException e) {

        }
        return userInfo;
    }

    /**
     * 消息推送
     *
     * @param openId
     * @param templateId
     */
    public String sendTemplateMsg(String openId, String templateId) {
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId)
                .templateId(templateId)
                .url("")
                .build();

        try {
            templateMessage.addData(new WxMpTemplateData("first", "value", "#FF00FF"))
                    .addData(new WxMpTemplateData("remark", "value", "#FF00FF"));
            return weChatPayment.getWxMpService().getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取模板列表
     */
    public List<WxMpTemplate> getTemplateList() throws Exception {
        return weChatPayment.getWxMpService().getTemplateMsgService().getAllPrivateTemplate();
    }


    /**
     * 获取公众号用户列表
     */
    public WxMpUserList getUserList(String nextOpenId) throws Exception {
        return weChatPayment.getWxMpService().getUserService().userList(nextOpenId);
    }

}
