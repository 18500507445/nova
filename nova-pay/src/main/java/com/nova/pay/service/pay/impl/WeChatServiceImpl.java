package com.nova.pay.service.pay.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateRequest;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.nova.common.constant.Constants;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.entity.param.WeChatMpParam;
import com.nova.pay.entity.result.FkPayConfig;
import com.nova.pay.entity.result.FkPayOrder;
import com.nova.pay.enums.PayWayEnum;
import com.nova.pay.service.fk.FkPayConfigService;
import com.nova.pay.service.fk.FkPayOrderService;
import com.nova.pay.service.pay.PayService;
import com.nova.pay.utils.open.WeChatUtil;
import com.nova.redis.core.RedisService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 微信支付实现类
 * @Author: wangzehui
 * @Date: 2022/9/20 17:43
 */
@Slf4j
@Service
public class WeChatServiceImpl implements PayService {

    @Autowired
    private WeChatUtil weChatUtil;

    @Autowired
    private FkPayConfigService fkPayConfigService;

    @Autowired
    private FkPayOrderService fkPayOrderService;

    @Resource
    private RedisService redisService;

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.WECHAT;
    }

    @Override
    public AjaxResult pay(PayParam param) {
        Object result = null;
        //入支付流水 1H5支付,2小程序,3app支付 4jsapi微信原生
        int type = Integer.parseInt(param.getType());
        String source = param.getSource();
        String sid = param.getSid();
        String userName = param.getUserName();
        String totalAmount = param.getTotalAmount();
        String openId = param.getOpenId();
        String orderId = param.getOrderId();
        Long payConfigId = param.getPayConfigId();
        String productId = param.getProductId();
        String subject = param.getSubject();
        int businessCode = param.getBusinessCode();
        if (ObjectUtil.hasEmpty(source, sid, payConfigId, orderId, type, userName, totalAmount, productId, subject)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        //原生和小程序需要先去获取openId
        if (ArrayUtils.contains(new int[]{2, 4}, type) && ObjectUtil.isEmpty(openId)) {
            return AjaxResult.error("1000", "openId为空");
        }
        try {
            //查询订单
            FkPayOrder payOrder = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, 2);
            if (ObjectUtil.isNotNull(payOrder)) {
                return AjaxResult.error("1000", "订单已存在,请从新下单");
            } else {
                //获取支付配置
                FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
                if (ObjectUtil.isNull(payConfig)) {
                    return AjaxResult.error("1000", "没有查询到支付方式");
                }
                FkPayOrder insert = FkPayOrder.builder().source(source)
                        .sid(sid)
                        .orderId(orderId)
                        .productId(productId)
                        .payConfigId(payConfigId)
                        .userName(userName)
                        .tradeStatus(0)
                        .payWay(2)
                        .type(type)
                        .businessCode(businessCode)
                        .fee(new BigDecimal(totalAmount)).build();
                int flag = fkPayOrderService.insertFkPayOrder(insert);
                if (0 == flag) {
                    return AjaxResult.error("1000", "创建支付订单失败,请从新下单");
                }
                if (ObjectUtil.isNull(payConfig.getApiV3Key())) {
                    result = weChatV2Pay(payConfig, param);
                } else {
                    result = weChatV3Pay(payConfig, param);
                }
            }
        } catch (Exception e) {
            log.error("weChatPay异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

    private Object weChatV2Pay(FkPayConfig payConfig, PayParam param) {
        Object result = "";
        //入支付流水 1H5支付,2小程序,3app支付 4jsapi微信原生
        int type = Integer.parseInt(param.getType());
        String openId = param.getOpenId();
        String orderId = param.getOrderId();
        String totalAmount = param.getTotalAmount();
        String subject = param.getSubject();
        try {
            //v2支付,注意本地调用keyPath换成自己的项目路径
            WxPayService wxV2PayService = weChatUtil.getWxV2PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getPaySecret(), payConfig.getKeyPath());
            //构造支付对象
            WxPayUnifiedOrderRequest.WxPayUnifiedOrderRequestBuilder requestBuilder = WxPayUnifiedOrderRequest.newBuilder().body(subject)
                    .spbillCreateIp(new BaseController().getIp())
                    .notifyUrl(payConfig.getNotifyUrl())
                    .outTradeNo(orderId)
                    .totalFee(NumberUtil.mul(totalAmount, "100").intValue());
            if (1 == type) {
                requestBuilder.tradeType(WxPayConstants.TradeType.MWEB);
            } else if (3 == type) {
                requestBuilder.tradeType(WxPayConstants.TradeType.APP);
            } else if (ArrayUtils.contains(new int[]{2, 4}, type)) {
                requestBuilder.tradeType(WxPayConstants.TradeType.JSAPI).openid(openId);
            }
            result = wxV2PayService.createOrder(requestBuilder.build());
        } catch (WxPayException e) {
            log.error("weChatV2Pay异常：{}", e.getMessage());
        }
        return result;
    }

    private Object weChatV3Pay(FkPayConfig payConfig, PayParam param) {
        Object result = "";
        //入支付流水 1H5支付,2小程序,3app支付 4jsapi微信原生
        int type = Integer.parseInt(param.getType());
        String openId = param.getOpenId();
        String orderId = param.getOrderId();
        String totalAmount = param.getTotalAmount();
        try {
            //v3支付
            WxPayService wxV3PayService = weChatUtil.getWxV3PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getApiV3Key(), payConfig.getSerialNo(), payConfig.getPrivateKeyPath(), payConfig.getPrivateCertPath());
            //构造支付对象
            WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
            request.setOutTradeNo(orderId);
            request.setNotifyUrl(payConfig.getNotifyUrl());
            request.setDescription(param.getSubject());
            WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
            amount.setCurrency("CNY");
            amount.setTotal(NumberUtil.mul(totalAmount, "100").intValue());
            request.setAmount(amount);
            if (1 == type) {
                result = wxV3PayService.createOrderV3(TradeTypeEnum.H5, request);
            } else if (3 == type) {
                result = wxV3PayService.createOrderV3(TradeTypeEnum.APP, request);
            } else if (ArrayUtils.contains(new int[]{2, 4}, type)) {
                WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
                payer.setOpenid(openId);
                request.setPayer(payer);
                result = wxV3PayService.createOrderV3(TradeTypeEnum.JSAPI, request);

            }
        } catch (WxPayException e) {
            log.error("weChatV3Pay异常：{}", e.getMessage());
        }
        return result;
    }

    @Override
    public AjaxResult refund(PayParam param) {
        Object result = "";
        String orderId = param.getOrderId();
        Long payConfigId = param.getPayConfigId();
        String totalAmount = param.getTotalAmount();
        if (ObjectUtil.hasEmpty(payConfigId, orderId, totalAmount)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        //获取支付配置
        FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        try {
            int fee = NumberUtil.mul(totalAmount, "100").intValue();
            if (ObjectUtil.isNull(payConfig.getApiV3Key())) {
                WxPayService wxV2PayService = weChatUtil.getWxV2PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getPaySecret(), payConfig.getKeyPath());
                WxPayRefundRequest request = new WxPayRefundRequest();
                request.setOutTradeNo(orderId);
                request.setOutRefundNo(orderId);
                request.setTotalFee(fee);
                request.setRefundFee(fee);
                result = wxV2PayService.refundV2(request);
            } else {
                WxPayService wxV3PayService = weChatUtil.getWxV3PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getApiV3Key(), payConfig.getSerialNo(), payConfig.getPrivateKeyPath(), payConfig.getPrivateCertPath());
                WxPayRefundV3Request request = new WxPayRefundV3Request();
                request.setOutTradeNo(orderId);
                WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
                amount.setCurrency("CNY");
                amount.setTotal(fee);
                amount.setRefund(fee);
                request.setAmount(amount);
                result = wxV3PayService.refundV3(request);
            }
        } catch (WxPayException e) {
            log.error("weChatPayRefund异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult queryOrder(PayParam param) {
        String orderId = param.getOrderId();
        Integer payWay = param.getPayWay();
        Object result = "";
        if (ObjectUtil.hasEmpty(orderId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        FkPayOrder payOrder = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, payWay);
        if (ObjectUtil.isNull(payOrder)) {
            return AjaxResult.error("1000", "没有查询到订单信息");
        }
        //获取支付配置
        FkPayConfig payConfig = fkPayConfigService.getConfigData(payOrder.getPayConfigId());
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        try {
            if (ObjectUtil.isNull(payConfig.getApiV3Key())) {
                WxPayService wxV2PayService = weChatUtil.getWxV2PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getPaySecret(), payConfig.getKeyPath());
                WxPayOrderQueryRequest request = new WxPayOrderQueryRequest();
                request.setOutTradeNo(orderId);
                result = wxV2PayService.queryOrder(request);
            } else {
                WxPayService wxV3PayService = weChatUtil.getWxV3PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getApiV3Key(), payConfig.getSerialNo(), payConfig.getPrivateKeyPath(), payConfig.getPrivateCertPath());
                WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
                request.setOutTradeNo(orderId);
                result = wxV3PayService.queryOrderV3(request);
            }
        } catch (WxPayException e) {
            log.error("weChatPayQueryOrder异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

    /**
     * 根据网页授权码获取微信用户openId
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult getOpenId(PayParam param) {
        Object result = "";
        String authCode = param.getAuthCode();
        Long payConfigId = param.getPayConfigId();
        String userName = param.getUserName();
        Integer payWay = param.getPayWay();
        if (ObjectUtil.hasEmpty(payConfigId, authCode, userName, payWay)) {
            return AjaxResult.error("1000", "缺少必要参数authCode、payConfigId、userName、payWay");
        }
        //获取支付配置
        FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        try {
            String key = Constants.REDIS_KEY + "getOpenId_" + payWay + "_" + payConfigId + "_" + userName;
            Object o = redisService.get(key);
            if (ObjectUtil.isNotNull(o)) {
                result = o.toString();
            } else {
                WxMpService wxMpService = weChatUtil.getWxMpService(payConfig.getAppId(), payConfig.getAppSecret());
                WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(authCode);
                if (ObjectUtil.isNotNull(accessToken)) {
                    //验证授权凭证失败 刷新后获取
                    boolean b = wxMpService.getOAuth2Service().validateAccessToken(accessToken);
                    if (!b) {
                        accessToken = wxMpService.getOAuth2Service().refreshAccessToken(accessToken.getRefreshToken());
                    }
                    result = accessToken.getOpenId();
                    redisService.set(key, result, 7000L);
                }
                log.debug("weChatPayGetOpenId返回：{}", JSONUtil.toJsonStr(accessToken));
            }
        } catch (WxErrorException e) {
            log.error("weChatPayGetOpenId异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

    /**
     * 关闭订单
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult closeOrder(PayParam param) {
        Object result = "";
        Long payConfigId = param.getPayConfigId();
        String orderId = param.getOrderId();
        if (ObjectUtil.hasEmpty(payConfigId, orderId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        //获取支付配置
        FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        try {
            if (ObjectUtil.isNull(payConfig.getApiV3Key())) {
                WxPayService wxV2PayService = weChatUtil.getWxV2PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getPaySecret(), payConfig.getKeyPath());
                result = wxV2PayService.closeOrder(orderId);
            } else {
                WxPayService wxV3PayService = weChatUtil.getWxV3PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getApiV3Key(), payConfig.getSerialNo(), payConfig.getPrivateKeyPath(), payConfig.getPrivateCertPath());
                wxV3PayService.closeOrderV3(orderId);
            }
        } catch (WxPayException e) {
            log.error("weChatPayCloseOrder异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

    /**
     * 商家转账到个人
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult merchantTransfer(PayParam param) {
        Object result = "";
        Long payConfigId = param.getPayConfigId();
        String openId = param.getOpenId();
        String orderId = param.getOrderId();
        String totalAmount = param.getTotalAmount();
        String subject = param.getSubject();
        if (ObjectUtil.hasEmpty(payConfigId, orderId, openId, totalAmount, subject)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        //获取支付配置
        FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig) && StrUtil.isNotBlank(payConfig.getApiV3Key())) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        try {
            WxPayService wxV3PayService = weChatUtil.getWxV3PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getApiV3Key(), payConfig.getSerialNo(), payConfig.getPrivateKeyPath(), payConfig.getPrivateCertPath());
            TransferCreateRequest request = new TransferCreateRequest();
            request.setBatchName(subject);
            request.setBatchRemark(subject);
            request.setTotalAmount(NumberUtil.mul(totalAmount, "100").intValue());
            request.setTotalNum(1);
            request.setOutBatchNo("batch" + orderId);

            List<TransferCreateRequest.TransferDetailList> list = new ArrayList<>();
            TransferCreateRequest.TransferDetailList data = new TransferCreateRequest.TransferDetailList();
            data.setOutDetailNo("detail" + orderId);
            data.setTransferAmount(1);
            data.setTransferRemark(subject);
            data.setOpenid(openId);

            list.add(data);
            request.setTransferDetailList(list);

            result = wxV3PayService.getMerchantTransferService().createTransfer(request);
        } catch (Exception e) {
            log.error("merchantTransfer异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

    /**
     * 通过code获得基本用户信息
     *
     * @param param
     * @return
     */
    public AjaxResult getOAuth2UserInfo(WeChatMpParam param) {
        log.info("getOAuth2UserInfo请求入参：{}", JSONUtil.toJsonStr(param));
        WxMpUser wxMpUser = null;
        String authCode = param.getAuthCode();
        String lang = param.getLang();
        Long payConfigId = param.getPayConfigId();
        if (ObjectUtil.hasEmpty(authCode, payConfigId)) {
            return AjaxResult.error("1000", "缺少必要参数authCode或payConfigId");
        }
        try {
            FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
            WxMpService wxMpService = weChatUtil.getWxMpService(payConfig.getAppId(), payConfig.getAppSecret());
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(authCode);
            wxMpUser = wxMpService.getUserService().userInfo(accessToken.getOpenId(), lang);
        } catch (WxErrorException e) {
            log.info("getOAuth2UserInfo异常：{}", e.getMessage());
        }
        return AjaxResult.success(wxMpUser);
    }
}
