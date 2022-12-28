package com.nova.pay.service.fk.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nova.common.constant.Constants;
import com.nova.common.utils.http.HttpRequestProxy;
import com.nova.common.utils.jax.Md5Utils;
import com.nova.pay.service.fk.FkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/8/24 14:30
 */
@Slf4j
@Service
public class FkOrderServiceImpl implements FkOrderService {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private JmsTemplate jmsTemplate;

    /**
     * 从cpapi获取orderId
     *
     * @param map
     * @return
     */
    @Override
    public String getOrderId(Map<String, String> map) {
        String orderId = "";
        String json = HttpUtil.createPost(Constants.CPAPI_URL)
                .formStr(map)
                .execute()
                .body();
        if (StrUtil.isNotEmpty(json)) {
            JSONObject jsonObject = JSONUtil.parseObj(json);
            if (jsonObject.containsKey("code") && StrUtil.equals("0000", jsonObject.getStr("code"))) {
                orderId = jsonObject.getStr("orderId");
            }
        }
        return orderId;
    }

    /**
     * 通用请求cpapi方法
     *
     * @param map
     * @return
     */
    @Override
    public Map<String, String> getCommon(Map<String, String> map) {
        Map<String, String> result = new HashMap<>(16);
        String json = HttpUtil.createPost(Constants.CPAPI_URL)
                .formStr(map)
                .execute()
                .body();
        if (StrUtil.isNotBlank(json)) {
            JSONObject jsonObject = JSONUtil.parseObj(json);
            if (jsonObject.containsKey("code")) {
                String code = jsonObject.getStr("code");
                if (StrUtil.equals("0000", code)) {
                    code = "0";
                }
                result.put("code", code);
            }
            if (jsonObject.containsKey("data")) {
                result.put("data", jsonObject.getStr("data"));
            }
            if (jsonObject.containsKey("message")) {
                result.put("msg", jsonObject.getStr("message"));
            }
            if (jsonObject.containsKey("msg")) {
                result.put("msg", jsonObject.getStr("msg"));
            }
        }
        return result;
    }

    /**
     * 发送消息到web进行加款
     * sign:(orderId+tradeStatus+payType+amount+userName+key)
     *
     * @param orderId
     * @param userName
     * @param tradeStatus -1失败 1成功
     * @param amount      单位元
     * @param payType
     * @return
     */
    public void recharge(String orderId, String userName, String tradeStatus, String amount, String payType) {
        //拼接签名调用web进行加款
        String param = orderId + tradeStatus + payType + amount + userName + Constants.MD5_KEY;
        String sign = Md5Utils.hash(param);
        log.info("recharge--orderId:{},userName:{},tradeStatus:{},amount:{},payType:{},sign:{}", orderId, userName, tradeStatus, amount, payType, sign);
        threadPoolTaskExecutor.execute(() -> {
            Map<String, Object> message = new HashMap<>(16);
            message.put("URL", Constants.PAY_URL);
            message.put("ORDERNO", orderId);
            message.put("TRANSTAT", tradeStatus);
            message.put("ORDERAMT", amount);
            message.put("USERNAME", userName);
            message.put("PAYTYPE", payType);
            message.put("SIGN", sign);
            jmsTemplate.convertAndSend(Constants.DESTINATION_NAME_RECHARGE, message);
        });
    }

    /**
     * 成功订单处理
     *
     * @param businessCode
     * @param orderId
     * @param userName
     * @param tradeStatus
     * @param amount
     * @param payType
     */
    @Override
    public void successOrderHandler(String source, String sid, int businessCode, String orderId, String userName, String tradeStatus, String amount, String payType) {
        //先充值 除了海外版
        if (businessCode > 0) {
            recharge(orderId, userName, tradeStatus, amount, payType);
        }
        switch (businessCode) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                openUserVip(source, sid, userName, businessCode);
                break;
            case 14:
                openExclusiveVip(sid, userName, amount, "1");
                break;
            case 15:
                openExclusiveVip(sid, userName, amount, "2");
                break;
            case 16:
                openExclusiveVip(sid, userName, amount, "3");
                break;
            default:
        }
    }

    /**
     * 调用cpapi开通尊享会员
     *
     * @param userName
     * @param fee
     * @param type     1白银 2黄金 3升级
     * @return
     */
    public void openExclusiveVip(String sid, String userName, String fee, String type) {
        log.info("openExclusiveVip--userName:{},fee:{},type:{}", userName, fee, type);
        //延迟1s防止加款没走完导致扣费展示余额不足
        ThreadUtil.safeSleep(1000);
        threadPoolTaskExecutor.execute(() -> {
            Map<String, String> param = new HashMap<>(16);
            param.put("function", "openExclusiveVip");
            param.put("userName", userName);
            param.put("fee", fee);
            param.put("sid", sid);
            param.put("operator", "fk_pay_center");
            param.put("remark", "充值开通");
            param.put("type", type);
            this.getCommon(param);
        });
    }

    /**
     * 开通vip会员
     */
    public void openUserVip(String source, String sid, String userName, int businessCode) {
        log.info("openUserVip--source:{},sid:{},userName:{},businessCode:{}", source, sid, userName, businessCode);
        ThreadUtil.safeSleep(1000);
        threadPoolTaskExecutor.execute(() -> {

        });
    }
}
