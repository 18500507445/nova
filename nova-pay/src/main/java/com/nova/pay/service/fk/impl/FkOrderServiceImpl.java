package com.nova.pay.service.fk.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nova.common.utils.Md5Utils;
import com.nova.common.utils.http.HttpRequestProxy;
import com.nova.common.constant.PayConstants;
import com.nova.pay.service.fk.FkOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/8/24 14:30
 */
@Service
public class FkOrderServiceImpl implements FkOrderService {

    private static final Logger logger = LoggerFactory.getLogger(FkOrderServiceImpl.class);

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
        String json = HttpRequestProxy.doPost(PayConstants.CPAPI_URL, map, "UTF-8", "UTF-8");
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
        String json = HttpRequestProxy.doPost(PayConstants.CPAPI_URL, map, "UTF-8", "UTF-8");
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
    @Override
    public void recharge(String orderId, String userName, String tradeStatus, String amount, String payType) {
        //拼接签名调用web进行加款
        String param = orderId + tradeStatus + payType + amount + userName + PayConstants.MD5_KEY;
        String sign = Md5Utils.hash(param);
        logger.info("recharge--orderId:{},userName:{},tradeStatus:{},amount:{},payType:{},sign:{}", orderId, userName, tradeStatus, amount, payType, sign);
        threadPoolTaskExecutor.execute(() -> {
            Map<String, Object> message = new HashMap<>(16);
            message.put("URL", PayConstants.PAY_URL);
            message.put("ORDERNO", orderId);
            message.put("TRANSTAT", tradeStatus);
            message.put("ORDERAMT", amount);
            message.put("USERNAME", userName);
            message.put("PAYTYPE", payType);
            message.put("SIGN", sign);
            jmsTemplate.convertAndSend(PayConstants.DESTINATION_NAME_RECHARGE, message);
        });
    }

    /**
     * 调用cpapi开通尊享会员
     *
     * @param userName
     * @param fee
     * @return
     */
    @Override
    public String openExclusiveVip(String userName, String fee) {
        Map<String, String> param = new HashMap<>(16);
        param.put("function", "openExclusiveVip");
        param.put("userName", userName);
        param.put("fee", fee);
        param.put("operator", "fk_pay_center");
        param.put("remark", "充值开通");
        //延迟200ms防止加款没走完导致扣费展示余额不足
        ThreadUtil.safeSleep(200);
        String json = HttpRequestProxy.doPost(PayConstants.CPAPI_URL, param, "UTF-8", "UTF-8");
        if (StrUtil.isNotEmpty(json)) {

        }
        return null;
    }
}
