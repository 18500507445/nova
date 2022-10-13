package com.nova.pay.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nova.common.constant.Constants;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.entity.result.FkPayList;
import com.nova.pay.enums.PayWayEnum;
import com.nova.pay.service.fk.FkOrderService;
import com.nova.pay.service.fk.FkPayListService;
import com.nova.pay.service.pay.PayStrategy;
import com.nova.redis.core.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 支付controller
 * @Author: wangzehui
 * @Date: 2022/8/21 20:56
 */
@RestController
@RequestMapping("/api/pay/")
public class PayController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private FkPayListService fkPayListService;

    @Autowired
    private PayStrategy payStrategy;

    @Autowired
    private FkOrderService fkOrderService;

    /**
     * 支付列表
     *
     * @return
     */
    @PostMapping("list")
    @ResponseBody
    public AjaxResult list(PayParam param) {
        logger.info("payList请求入参：{}", JSONUtil.toJsonStr(param));
        boolean redisFlag = Boolean.TRUE;
        String source = param.getSource();
        String sid = param.getSid();
        String clientType = param.getClientType();
        String userName = param.getUserName();
        if (StringUtils.isBlank(source) || StringUtils.isBlank(sid)) {
            return AjaxResult.error("1000", "必要参数sid或source为空");
        }
        List<Map<String, String>> list = new ArrayList<>();
        String key = Constants.REDIS_KEY + "payController_list_" + source + "_" + sid;
        Object o = redisService.get(key);
        if (null != o) {
            list = JSON.parseObject(JSONObject.toJSONString(redisService.get(key)), new TypeReference<List<Map<String, String>>>() {
            });
        } else {
            FkPayList query = FkPayList.builder().source(source).sid(sid).build();
            List<FkPayList> payList = fkPayListService.selectFkPayListList(query);
            if (CollUtil.isNotEmpty(payList)) {
                for (FkPayList data : payList) {
                    Integer wechatH5 = data.getWechatH5();
                    Integer wechatJsapi = data.getWechatJsapi();
                    Integer wechatApp = data.getWechatApp();
                    String wechatLogoUrl = data.getWechatLogoUrl();
                    if (1 == wechatH5 || 1 == wechatJsapi || 1 == wechatApp) {
                        Map<String, String> weChatMap = new HashMap<>(16);
                        weChatMap.put("name", "微信");
                        weChatMap.put("url", wechatLogoUrl);
                        weChatMap.put("logoUrl", wechatLogoUrl);
                        weChatMap.put("content", "如遇到问题，请换其它支付方式");
                        weChatMap.put("freeContent", "(推荐)");
                        if (1 == wechatH5) {
                            weChatMap.put("type", "1");
                        } else if (1 == wechatJsapi) {
                            weChatMap.put("type", "4");
                        } else {
                            weChatMap.put("type", "3");
                        }
                        weChatMap.put("payWay", "2");
                        list.add(weChatMap);
                    }
                    Integer aliApp = data.getAliApp();
                    Integer aliH5 = data.getAliH5();
                    Integer aliApplet = data.getAliApplet();
                    String aliLogoUrl = data.getAliLogoUrl();
                    if (1 == aliApp || 1 == aliH5 || 1 == aliApplet) {
                        Map<String, String> aLiMap = new HashMap<>(16);
                        aLiMap.put("name", "支付宝");
                        aLiMap.put("url", aliLogoUrl);
                        aLiMap.put("logoUrl", aliLogoUrl);
                        aLiMap.put("content", "支付宝推荐，安全快捷");
                        aLiMap.put("freeContent", "(免手续费)");
                        if (1 == aliApp) {
                            aLiMap.put("type", "3");
                        } else if (1 == aliH5) {
                            aLiMap.put("type", "1");
                        } else {
                            aLiMap.put("type", "2");
                        }
                        aLiMap.put("payWay", "1");
                        list.add(aLiMap);
                    }
                    Integer yeePayQuick = data.getYeePayQuick();
                    String yeePayLogoUrl = data.getYeePayLogoUrl();
                    if (1 == yeePayQuick) {
                        Map<String, String> yeeMap = new HashMap<>(16);
                        yeeMap.put("name", "银行卡支付");
                        yeeMap.put("logoUrl", yeePayLogoUrl);
                        yeeMap.put("content", "免手续费，1万/笔，2万/日，5万/月");
                        yeeMap.put("freeContent", "(推荐)");
                        yeeMap.put("type", "7");
                        yeeMap.put("payWay", "4");
                        list.add(yeeMap);
                    }
                    if (StrUtil.equals("fengkuangTY", clientType) && ObjectUtil.equals(1, data.getBallCoin()) && StrUtil.isNotBlank(userName)) {
                        Map<String, String> ballCoinMap = new HashMap<>(16);
                        ballCoinMap.put("name", "球币兑换");
                        ballCoinMap.put("logoUrl", data.getBallCoinLogoUrl());
                        Map<String, String> map = new HashMap<>();
                        map.put("function", "findUserInfoByUserName");
                        map.put("user_name", userName);
                        Map<String, String> userMap = fkOrderService.getCommon(map);
                        if (!userMap.isEmpty()) {
                            String jsonData = MapUtil.getStr(userMap, "data", "");
                            if (StrUtil.isNotBlank(jsonData)) {
                                String userFee = JSONUtil.parseObj(jsonData).getStr("user_valid_fee", "0");
                                if (userFee.startsWith(".")) {
                                    userFee = "0" + userFee;
                                }
                                String content = "（余额" + userFee + "球币）";
                                ballCoinMap.put("content", content);
                                ballCoinMap.put("freeContent", content);
                            }
                        }
                        ballCoinMap.put("type", "8");
                        ballCoinMap.put("payWay", "5");
                        list.add(ballCoinMap);
                        redisFlag = Boolean.FALSE;
                    }
                }
                if (redisFlag) {
                    redisService.set(key, list, 60 * 10L);
                }
            }
        }
        return AjaxResult.success(list);
    }

    /**
     * 微信v2支付
     */
    @PostMapping("weChatV2Pay")
    @ResponseBody
    @Deprecated
    public AjaxResult weChatV2Pay(PayParam param) {
        AjaxResult result = new AjaxResult();
        logger.info("weChatV2Pay请求入参：{}", JSONUtil.toJsonStr(param));
        try {
            result = payStrategy.pay(PayWayEnum.WECHAT, param);
        } finally {
            logger.info("weChatV2Pay返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }

    /**
     * 支付宝pay
     */
    @PostMapping("aLiPay")
    @ResponseBody
    @Deprecated
    public AjaxResult aLiPay(PayParam param) {
        AjaxResult result = new AjaxResult();
        logger.info("aLiPay请求入参：{}", JSONUtil.toJsonStr(param));
        try {
            result = payStrategy.pay(PayWayEnum.ALI, param);
        } finally {
            logger.info("aLiPay返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }

    @PostMapping("applePay")
    @ResponseBody
    @Deprecated
    public AjaxResult applePay(PayParam param) {
        logger.info("applePay请求入参：{}", JSONUtil.toJsonStr(param));
        return payStrategy.pay(PayWayEnum.ALI, param);
    }

    /**
     * 公共支付
     *
     * @param param
     * @return
     */
    @PostMapping("openPay")
    @ResponseBody
    public AjaxResult openPay(PayParam param) {
        logger.info("openPay请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.pay(PayWayEnum.valuesOf(payWay), param);
        } finally {
            logger.info("openPay返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }

    /**
     * 关闭订单
     *
     * @param param
     * @return
     */
    @PostMapping("closeOrder")
    @ResponseBody
    public AjaxResult closeOrder(PayParam param) {
        logger.info("closeOrder请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.closeOrder(PayWayEnum.valuesOf(payWay), param);
        } finally {
            logger.info("closeOrder返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }

    /**
     * 查询订单
     *
     * @param param
     * @return
     */
    @PostMapping("queryOrder")
    @ResponseBody
    public AjaxResult queryOrder(PayParam param) {
        logger.info("queryOrder请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.queryOrder(PayWayEnum.valuesOf(payWay), param);
        } finally {
            logger.info("queryOrder返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }

    /**
     * 获取openId
     *
     * @param param
     * @return
     */
    @PostMapping("getOpenId")
    @ResponseBody
    public AjaxResult getOpenId(PayParam param) {
        logger.info("getOpenId请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.getOpenId(PayWayEnum.valuesOf(payWay), param);
        } finally {
            logger.info("getOpenId返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }


    /**
     * 退款
     *
     * @param param
     * @return
     */
    @PostMapping("refund")
    @ResponseBody
    public AjaxResult refund(PayParam param) {
        logger.info("refund请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.refund(PayWayEnum.valuesOf(payWay), param);
        } finally {
            logger.info("refund返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }


    /**
     * 商家转账到个人
     *
     * @param param
     * @return
     */
    @PostMapping("merchantTransfer")
    @ResponseBody
    public AjaxResult merchantTransfer(PayParam param) {
        logger.info("merchantTransfer请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.merchantTransfer(PayWayEnum.valuesOf(payWay), param);
        } finally {
            logger.info("merchantTransfer返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }

}
