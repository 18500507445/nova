package com.nova.shopping.pay.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.nova.shopping.common.config.RedisService;
import com.nova.shopping.common.constant.BaseController;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.common.constant.result.AjaxResult;
import com.nova.shopping.common.enums.PayWayEnum;
import com.nova.shopping.pay.entity.MyPayList;
import com.nova.shopping.pay.entity.MyPayOrder;
import com.nova.shopping.pay.entity.param.PayParam;
import com.nova.shopping.pay.service.MyPayListService;
import com.nova.shopping.pay.service.MyPayOrderService;
import com.nova.shopping.pay.service.pay.PayStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 支付controller
 * @author: wzh
 * @date: 2023/3/21 20:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay/")
public class PayController extends BaseController {

    private final RedisService redisService;

    private final MyPayListService myPayListService;

    private final PayStrategy payStrategy;

    private final MyPayOrderService myPayOrderService;

    /**
     * 支付列表
     *
     * @return
     */
    @PostMapping("list")
    public AjaxResult list(PayParam param) {
        log.info("payList请求入参：{}", JSONUtil.toJsonStr(param));
        boolean redisFlag = Boolean.TRUE;
        String source = param.getSource();
        String sid = param.getSid();
        String clientType = param.getClientType();
        String userName = param.getUserName();
        if (StringUtils.isBlank(source) || StringUtils.isBlank(sid)) {
            return AjaxResult.error("1000", "必要参数sid或source为空");
        }
        List<Map<String, String>> list = new ArrayList<>();
        String key = Constants.REDIS_KEY + "payController_list_" + source + "_" + sid + "_" + clientType;
        Object o = redisService.get(key);
        if (ObjectUtil.isNotNull(o)) {
            list = JSON.parseObject(o.toString(), new TypeReference<List<Map<String, String>>>() {
            });
        } else {
            MyPayList query = MyPayList.builder().source(source).sid(sid).build();
            List<MyPayList> payList = myPayListService.selectMyPayListList(query);
            if (CollUtil.isNotEmpty(payList)) {
                for (MyPayList data : payList) {
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
                    //易宝支付
                    Integer yeePayQuick = data.getYeePayQuick();
                    String yeePayLogoUrl = data.getYeePayLogoUrl();
                    if (1 == yeePayQuick) {
                        Map<String, String> yeeMap = MapUtil.builder(new HashMap<String, String>(16))
                                .put("name", "银行卡支付")
                                .put("logoUrl", yeePayLogoUrl)
                                .put("content", "免手续费，1万/笔，2万/日，5万/月")
                                .put("freeContent", "(推荐)")
                                .put("type", "7")
                                .put("payWay", "4").build();
                        list.add(yeeMap);
                    }
                    //谷歌支付
                    if (1 == data.getGooglePay()) {
                        Map<String, String> googleMap = MapUtil.builder(new HashMap<String, String>(16))
                                .put("name", "谷歌支付")
                                .put("logoUrl", data.getGoogleLogoUrl())
                                .put("content", "")
                                .put("freeContent", "(推荐)")
                                .put("type", "3")
                                .put("payWay", "5").build();
                        list.add(googleMap);
                    }
                }
                if (redisFlag) {
                    redisService.set(key, JSONUtil.toJsonStr(list), 60 * 30L);
                }
            }
        }
        return AjaxResult.success(list);
    }

    /**
     * 微信v2支付
     */
    @PostMapping("weChatV2Pay")
    @Deprecated
    public AjaxResult weChatV2Pay(PayParam param) {
        AjaxResult result = new AjaxResult();
        log.info("weChatV2Pay请求入参：{}", JSONUtil.toJsonStr(param));
        try {
            result = payStrategy.pay(PayWayEnum.WECHAT, param);
        } finally {
            log.info("weChatV2Pay返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }

    /**
     * 支付宝pay
     */
    @PostMapping("aLiPay")
    @Deprecated
    public AjaxResult aLiPay(PayParam param) {
        AjaxResult result = new AjaxResult();
        log.info("aLiPay请求入参：{}", JSONUtil.toJsonStr(param));
        try {
            result = payStrategy.pay(PayWayEnum.ALI, param);
        } finally {
            log.info("aLiPay返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }

    @PostMapping("applePay")
    @Deprecated
    public AjaxResult applePay(PayParam param) {
        log.info("applePay请求入参：{}", JSONUtil.toJsonStr(param));
        return payStrategy.pay(PayWayEnum.APPLE, param);
    }

    /**
     * 公共支付
     *
     * @param param
     * @return
     */
    @PostMapping("openPay")
    public AjaxResult openPay(PayParam param) {
        log.info("openPay请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.pay(PayWayEnum.valuesOf(payWay), param);
        } finally {
            log.info("openPay返回：{}", JSONUtil.toJsonStr(result));
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
    public AjaxResult closeOrder(PayParam param) {
        log.info("closeOrder请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.closeOrder(PayWayEnum.valuesOf(payWay), param);
        } finally {
            log.info("closeOrder返回：{}", JSONUtil.toJsonStr(result));
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
    public AjaxResult queryOrder(PayParam param) {
        log.info("queryOrder请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.queryOrder(PayWayEnum.valuesOf(payWay), param);
        } finally {
            log.info("queryOrder返回：{}", JSONUtil.toJsonStr(result));
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
    public AjaxResult getOpenId(PayParam param) {
        log.info("getOpenId请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.getOpenId(PayWayEnum.valuesOf(payWay), param);
        } finally {
            log.info("getOpenId返回：{}", JSONUtil.toJsonStr(result));
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
    public AjaxResult refund(PayParam param) {
        log.info("refund请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            String orderId = param.getOrderId();
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            if (StrUtil.isBlank(orderId)) {
                return AjaxResult.error("1000", "缺少参数：orderId");
            }
            MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, payWay);
            if (ObjectUtil.isNull(payOrder)) {
                return AjaxResult.error("1000", "没有查询到订单信息");
            }
            if (!ObjectUtil.equals(1, payOrder.getTradeStatus())) {
                return AjaxResult.error("1000", "非成功订单不能进行退款操作");
            }
            param.setPayConfigId(payOrder.getPayConfigId());
            result = payStrategy.refund(PayWayEnum.valuesOf(payWay), param);
        } finally {
            log.info("refund返回：{}", JSONUtil.toJsonStr(result));
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
    public AjaxResult merchantTransfer(PayParam param) {
        log.info("merchantTransfer请求入参：{}", JSONUtil.toJsonStr(param));
        AjaxResult result = new AjaxResult();
        try {
            Integer payWay = param.getPayWay();
            if (ObjectUtil.isNull(payWay)) {
                return AjaxResult.error("1000", "缺少参数：payWay（支付方式）");
            }
            result = payStrategy.merchantTransfer(PayWayEnum.valuesOf(payWay), param);
        } finally {
            log.info("merchantTransfer返回：{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }

}
