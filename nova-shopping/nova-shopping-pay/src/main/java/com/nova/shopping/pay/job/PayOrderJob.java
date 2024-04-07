package com.nova.shopping.pay.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.PageHelper;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.common.constant.result.AjaxResult;
import com.nova.shopping.common.enums.PayWayEnum;
import com.nova.shopping.pay.repository.entity.PayConfig;
import com.nova.shopping.pay.repository.entity.PayOrder;
import com.nova.shopping.pay.web.dto.AliPayParam;
import com.nova.shopping.pay.web.dto.KsPayParam;
import com.nova.shopping.pay.web.dto.PayParam;
import com.nova.shopping.pay.web.dto.YeePayParam;
import com.nova.shopping.pay.payment.open.*;
import com.nova.shopping.pay.service.pay.PayConfigService;
import com.nova.shopping.pay.service.pay.PayOrderService;
import com.nova.shopping.pay.service.strategy.impl.KsPayServiceImpl;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: 支付订单job
 * @author: wzh
 * @date: 2023/3/24 14:00
 */
@Slf4j
//@Component
@RequiredArgsConstructor
public class PayOrderJob {

    private final PayOrderService payOrderService;

    private final PayConfigService payConfigService;

    //private final MyOrderService myOrderService;

    private final AliPayment aliPayment;

    private final WeChatPayment weChatPayment;

    private final YeePayment yeePayment;

    private final ApplePayment applePayment;

    private final KsPayment ksPayment;

    private final KsPayServiceImpl ksPayService;

    /**
     * 订单重试
     */
    @XxlJob("reTryOrderJob")
    public void reTryOrderJob() {
        int payFlag = 0;
        int updateFlag = 0;
        PageHelper.startPage(1, 20);
        PayOrder query = PayOrder.builder().tradeStatus(4).build();
        List<PayOrder> payOrderList = payOrderService.selectMyPayOrderList(query);
        try {
            if (CollUtil.isNotEmpty(payOrderList)) {
                XxlJobHelper.log("reTryOrderJobStart--处理条数:{}", payOrderList.size());
                for (PayOrder payOrder : payOrderList) {
                    String orderId = payOrder.getOrderId();
                    Long payConfigId = payOrder.getPayConfigId();
                    Integer payWay = payOrder.getPayWay();
                    PayConfig payConfig = payConfigService.getConfigData(payConfigId);
                    if (ObjectUtil.isNotNull(payConfig)) {
                        if (PayWayEnum.ALI.getPayWay() == payWay) {
                            AliPayParam aliPayParam = AliPayParam.builder().appId(payConfig.getAppId())
                                    .publicKey(payConfig.getPublicKey())
                                    .privateKey(payConfig.getPrivateKey())
                                    .outTradeNo(orderId).build();
                            AlipayTradeQueryResponse response = aliPayment.queryOrder(aliPayParam);
                            if (response.isSuccess() && StringUtils.equals(Constants.TRADE_SUCCESS, response.getTradeStatus())) {
                                payFlag = 1;
                            }
                        } else if (PayWayEnum.WECHAT.getPayWay() == payWay) {
                            if (ObjectUtil.isNull(payConfig.getApiV3Key())) {
                                WxPayService wxV2PayService = weChatPayment.getWxV2PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getPaySecret(), payConfig.getKeyPath());
                                WxPayOrderQueryRequest request = new WxPayOrderQueryRequest();
                                request.setOutTradeNo(orderId);
                                WxPayOrderQueryResult v2OrderResult = wxV2PayService.queryOrder(request);
                                if (ObjectUtil.isNotNull(v2OrderResult) && StrUtil.equals(Constants.SUCCESS, v2OrderResult.getResultCode()) && StrUtil.equals(Constants.SUCCESS, v2OrderResult.getTradeState())) {
                                    payFlag = 1;
                                }
                            } else {
                                WxPayService wxV3PayService = weChatPayment.getWxV3PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getApiV3Key(), payConfig.getSerialNo(), payConfig.getPrivateKeyPath(), payConfig.getPrivateCertPath());
                                WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
                                request.setOutTradeNo(orderId);
                                WxPayOrderQueryV3Result v3OrderResult = wxV3PayService.queryOrderV3(request);
                                if (ObjectUtil.isNotNull(v3OrderResult) && StrUtil.equals(Constants.SUCCESS, v3OrderResult.getTradeState())) {
                                    payFlag = 1;
                                }
                            }
                        } else if (PayWayEnum.APPLE.getPayWay() == payWay) {
                            Map<String, Object> appleMap = applePayment.verify(payOrder.getSign(), "", "");
                            String status = MapUtil.getStr(appleMap, "status");
                            String transactionId = MapUtil.getStr(appleMap, "transaction_id");
                            if (ArrayUtil.contains(new String[]{"0", "Real"}, status) && !StrUtil.equals(Constants.ZERO, transactionId)) {
                                payFlag = 1;
                            }
                        } else if (PayWayEnum.YEE_PAY.getPayWay() == payWay) {
                            YeePayParam yeePayParam = YeePayParam.builder().appKey(payConfig.getAppId())
                                    .privateKey(payConfig.getPrivateKey())
                                    .parentMerchantNo(payConfig.getMchId())
                                    .merchantNo(payConfig.getMchId())
                                    .orderId(orderId)
                                    .amount(payOrder.getFee().toPlainString())
                                    .tradeNo(payOrder.getTradeNo())
                                    .build();
                            Map<String, String> yeeMap = yeePayment.queryOrder(yeePayParam);
                            if (StrUtil.equals(Constants.SUCCESS, MapUtil.getStr(yeeMap, "status"))) {
                                payFlag = 1;
                            }
                        }
                        if (ObjectUtil.equal(1, payFlag)) {
                            payOrder.setTradeStatus(1);
                            updateFlag = payOrderService.updateMyPayOrder(payOrder);
                            if (updateFlag > 0) {
                                //myOrderService.successOrderHandler(payOrder.getSource(), payOrder.getSid(), payOrder.getBusinessCode(), orderId, payOrder.getUserName(), "1", payOrder.getFee().toString());
                            }
                        }
                        XxlJobHelper.log("reTryOrderJobProcess--orderId:{},payFlag:{}", orderId, payFlag);
                    }
                }
            }
        } catch (WxPayException e) {
            log.error("reTryOrderJob异常:{}", e.getMessage());
        }
        XxlJobHelper.log("reTryOrderJobEnd");
    }

    /**
     * 快手结算订单
     *
     * @see <a href="https://developers.kuaishou.com/topic?tid=5877&bizType=miniprogram">支付结算周期调整</a>
     */
    @XxlJob("ksSettleOrderJob")
    public void ksSettleOrderJob() {
        int i = 0;
        TimeInterval timer = DateUtil.timer();
        XxlJobHelper.log("快手结算订单job开始");
        try {
            PayOrder query = PayOrder.builder().payWay(6).tradeStatus(1).notEqualRemark("已结算").createTime(DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, -3)).build();
            List<PayOrder> orderList = payOrderService.selectMyPayOrderList(query);
            if (CollUtil.isNotEmpty(orderList)) {
                for (PayOrder payOrder : orderList) {
                    String orderId = payOrder.getOrderId();
                    PayConfig payConfig = payConfigService.selectMyPayConfigById(payOrder.getPayConfigId());
                    if (ObjectUtil.isNotNull(payConfig)) {
                        PayParam param = PayParam.builder().payConfigId(payOrder.getPayConfigId()).build();
                        AjaxResult accessTokenResult = ksPayService.getOpenId(param);
                        if (ObjectUtil.isNotNull(accessTokenResult)) {
                            JSONObject accessTokenJson = JSONUtil.parseObj(accessTokenResult);
                            if (accessTokenJson.containsKey("data") && StrUtil.isNotBlank(accessTokenJson.getStr("data"))) {
                                KsPayParam report = KsPayParam.builder()
                                        .appId(payConfig.getAppId())
                                        .appSecret(payConfig.getAppSecret())
                                        .accessToken(accessTokenJson.getStr("data"))
                                        .outOrderNo(orderId)
                                        .outSettleNo(orderId)
                                        .totalAmount(NumberUtil.mul(payOrder.getFee(), 100).longValue())
                                        .build();
                                Map<String, Object> settleMap = ksPayment.createSettle(report);
                                XxlJobHelper.log("settleMapJson:{}", JSONUtil.toJsonStr(settleMap));
                                i++;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("ksSettleOrderJob异常:" + e.getMessage());
        } finally {
            XxlJobHelper.log("快手结算订单job结束，处理" + i + "条，耗时" + timer.interval() + "ms...");
        }
    }

}
