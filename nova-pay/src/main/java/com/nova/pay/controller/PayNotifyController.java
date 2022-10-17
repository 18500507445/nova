package com.nova.pay.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.XmlConfig;
import com.nova.common.constant.Constants;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.result.FkPayConfig;
import com.nova.pay.entity.result.FkPayOrder;
import com.nova.pay.service.fk.FkOrderService;
import com.nova.pay.service.fk.FkPayConfigService;
import com.nova.pay.service.fk.FkPayOrderService;
import com.nova.pay.utils.open.AliPayUtil;
import com.nova.pay.utils.open.IosVerifyUtil;
import com.nova.pay.utils.open.WeChatUtil;
import com.nova.pay.utils.open.YeePayUtil;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.yeepay.shade.org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 支付通知controller
 * @Author: wangzehui
 * @Date: 2022/8/21 20:57
 */
@Slf4j
@RestController
@RequestMapping("/api/payNotify/")
public class PayNotifyController extends BaseController {

    @Resource
    private WeChatUtil weChatUtil;

    @Resource
    private AliPayUtil aliPayUtil;

    @Resource
    private IosVerifyUtil iosVerifyUtil;

    @Autowired
    private FkPayOrderService fkPayOrderService;

    @Autowired
    private FkOrderService fkOrderService;

    @Autowired
    private FkPayConfigService fkPayConfigService;

    /**
     * 支付宝通知
     */
    @PostMapping("aLiPay")
    public String aLiPay(HttpServletRequest request) {
        String orderId = "";
        String out = AliPayUtil.NOTIFY_FAIL;
        int payFlag = 0;
        Map<String, String> params = new HashMap<>(16);
        try {
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String str : requestParams.keySet()) {
                String[] values = requestParams.get(str);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                // TODO: 支付宝支持SDK支持三种编码格式，分别是：UTF-8，GBK，GB2312
                valueStr = new String(valueStr.getBytes(), StandardCharsets.UTF_8);
                params.put(str, valueStr);
            }
            String tradeStatus = MapUtil.getStr(params, "trade_status");
            //体育订单id,支付宝交易流水号
            orderId = MapUtil.getStr(params, "out_trade_no");
            String tradeNo = MapUtil.getStr(params, "trade_no");
            String totalAmount = MapUtils.getString(params, "total_amount");
            //查询订单
            FkPayOrder payOrder = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, 1);
            if (ObjectUtil.isNotNull(payOrder) && 1 != payOrder.getTradeStatus()) {
                FkPayOrder.FkPayOrderBuilder orderBuilder = FkPayOrder.builder().orderId(orderId).tradeNo(tradeNo).payWay(1);
                //回调改成:处理中
                fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(4).build());
                //查询配置参数，验签处理
                FkPayConfig payConfig = fkPayConfigService.getConfigData(payOrder.getPayConfigId());
                boolean check = aliPayUtil.rsaCheckV1(params, payConfig.getPublicKey());
                log.info("aLiPayNotify通知参数:{}------验签结果:{}", JSONObject.toJSONString(params), check);
                BigDecimal fee = payOrder.getFee();
                String userName = payOrder.getUserName();
                if (check) {
                    if (StringUtils.equals(AliPayUtil.TRADE_SUCCESS, tradeStatus) && ObjectUtil.equals(fee, new BigDecimal(totalAmount))) {
                        payFlag = fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(1).build());
                        if (payFlag > 0) {
                            String payType = payConfig.getPayType();
                            fkOrderService.recharge(orderId, userName, "1", fee.toString(), payType);
                            out = AliPayUtil.NOTIFY_SUCCESS;
                        }
                    } else {
                        payFlag = fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(2).build());
                    }
                }
            }
        } catch (Exception e) {
            log.error("aLiPayNotify异常:{}", e.getMessage());
        } finally {
            log.info("aLiPayNotify====>orderId:{}, payFlag:{}", orderId, payFlag);
        }
        return out;
    }


    /**
     * 微信V2通知
     */
    @PostMapping("weChatV2Pay")
    public String weChatV2Pay(HttpServletRequest request) {
        String orderId = "";
        String out = "";
        int payFlag = 0;
        XmlConfig.fastMode = true;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            StringBuilder xmlString = new StringBuilder();
            while ((line = br.readLine()) != null) {
                xmlString.append(line);
            }
            //解析xml转对象
            WxPayOrderNotifyResult notifyResult = BaseWxPayResult.fromXML(xmlString.toString(), WxPayOrderNotifyResult.class);
            log.info("weChatV2PayNotify：{}", JSONUtil.toJsonStr(notifyResult));
            orderId = notifyResult.getOutTradeNo();
            String tradeNo = notifyResult.getTransactionId();
            //查询订单
            FkPayOrder payOrder = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, 2);
            if (ObjectUtil.isNotNull(payOrder)) {
                FkPayOrder.FkPayOrderBuilder orderBuilder = FkPayOrder.builder().orderId(orderId).tradeNo(tradeNo).payWay(2);
                //回调改成:处理中
                fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(4).build());
                //查询配置参数，验签处理
                FkPayConfig payConfig = fkPayConfigService.getConfigData(payOrder.getPayConfigId());
                if (ObjectUtil.isNotNull(payConfig) && 1 != payOrder.getTradeStatus()) {
                    WxPayService wxV2PayService = weChatUtil.getWxV2PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getPaySecret(), payConfig.getKeyPath());
                    notifyResult = wxV2PayService.parseOrderNotifyResult(xmlString.toString());
                    BigDecimal fee = payOrder.getFee();
                    String userName = payOrder.getUserName();
                    if (StrUtil.equals(WxPayConstants.ResultCode.SUCCESS, notifyResult.getReturnCode())) {
                        payFlag = fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(1).build());
                        if (payFlag > 0) {
                            String payType = payConfig.getPayType();
                            fkOrderService.recharge(orderId, userName, "1", fee.toString(), payType);
                        }
                    } else {
                        payFlag = fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(2).build());
                    }
                }
            }
            out = WxPayNotifyResponse.success("OK");
        } catch (IOException | WxPayException e) {
            String message = e.getMessage();
            out = WxPayNotifyResponse.success(message);
            log.error("weChatV2PayNotify异常:{}", message);
        } finally {
            XmlConfig.fastMode = false;
            log.info("weChatV2PayNotify====>orderId:{}, payFlag:{}", orderId, payFlag);
        }
        return out;
    }

    /**
     * 微信v3通知
     */
    public void weChatV3Pay(HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     * 苹果通知
     */
    @PostMapping("applePay")
    public AjaxResult applePay(HttpServletRequest request) {
        String result = "ok";
        int payFlag = 0;
        String orderId = "";
        Map<String, String> params = new HashMap<>(16);
        try {
            //1.0 处理苹果通知过来的请求信息
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String str : requestParams.keySet()) {
                String[] values = requestParams.get(str);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用
                valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                params.put(str, valueStr);
            }
            orderId = MapUtil.getStr(params, "trade_no");
            String sign = MapUtil.getStr(params, "sign");
            String sid = MapUtil.getStr(params, "sid");
            String version = getValue("version");
            params.put("version", version);
            log.info("applePay通知:{}", JSONObject.toJSONString(params));
            //查询订单
            FkPayOrder payOrder = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, 3);
            if (ObjectUtil.isNotNull(payOrder) && 1 != payOrder.getTradeStatus()) {
                FkPayOrder.FkPayOrderBuilder orderBuilder = FkPayOrder.builder().orderId(orderId).payWay(3).sign(sign);
                //回调改成:处理中
                fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(4).remark("false").build());
                //查询配置参数，验签处理
                FkPayConfig payConfig = fkPayConfigService.getConfigData(payOrder.getPayConfigId());
                //2.0 然后进行验证
                Map<String, Object> verify = iosVerifyUtil.verify(sign, sid, version);
                String transactionId = MapUtil.getStr(verify, "transaction_id");
                String status = MapUtil.getStr(verify, "status");
                boolean check = ArrayUtils.contains(new String[]{"0"}, status) && !StringUtils.equals(Constants.ZERO, transactionId);
                log.info("applePayNotify验签结果====>orderId:{}, verify:{}, check:{}", orderId, verify, check);
                if (check && ObjectUtil.isEmpty(payOrder.getTradeNo())) {
                    //3.0 通知修改成处理中,存入sign、验签结果
                    payFlag = fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(1).tradeNo(transactionId).remark("ture").build());
                    if (payFlag > 0) {
                        BigDecimal fee = payOrder.getFee();
                        String userName = payOrder.getUserName();
                        String payType = payConfig.getPayType();
                        fkOrderService.recharge(orderId, userName, "1", fee.toString(), payType);
                    }
                } else {
                    payFlag = fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(2).build());
                }
            }
        } catch (Exception e) {
            log.error("applePayNotify异常:{}", e.getMessage());
        } finally {
            log.info("applePayNotify====>orderId:{}, payFlag:{}", orderId, payFlag);
        }
        return AjaxResult.success(result);
    }

    /**
     * 一级易宝支付结果通知
     * https://open.yeepay.com/docs/apis/ptssfk/jiaoyi/options__rest__v1.0__trade__order#anchor7
     * {"channelOrderId":"332022021622001422971445123456","orderId":"DQD620CUD57Q8A3123456","bankOrderId":"ST5207953516123456","paySuccessDate":"2022-02-16 20:26:05","channel":"ALIPAY","payWay":"MINI_PROGRAM","uniqueOrderNo":"1013202202160000003222123456","orderAmount":"2.00","payAmount":"2.00","payerInfo":"{\"bankCardNo\":\"\",\"bankId\":\"ALIPAY\",\"buyerLogonId\":\"136****1013\",\"mobilePhoneNo\":\"\",\"userID\":\"2088502619123456\"}","realPayAmount":"2.00","parentMerchantNo":"10085123456","merchantNo":"10085123456","status":"SUCCESS"}
     */
    @PostMapping("yeePay")
    public void yeePay(HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        int payFlag = 0;
        String orderId = "";
        DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
        try {
            String appKey = request.getParameter("customerIdentification");
            String encrypt = request.getParameter("response");
            log.info("yeePay获取到的appKey为{},response为{}", appKey, encrypt);
            dto.setCipherText(encrypt);
            FkPayConfig payConfig = fkPayConfigService.selectFkPayConfigById(2L);
            if (ObjectUtil.isNotNull(payConfig)) {
                dto = DigitalEnvelopeUtils.decrypt(dto, YeePayUtil.getPrivateKey(payConfig.getPrivateKey()), YeePayUtil.getPubKey(payConfig.getPublicKey()));
                String plainText = dto.getPlainText();
                if (StringUtils.isNotBlank(plainText)) {
                    JSONObject json = JSONObject.parseObject(plainText);
                    log.info("易宝支付结果通知解析json:{}", json.toJSONString());
                    //易宝收款订单号
                    orderId = json.getString("orderId");
                    String outTradeNo = json.getString("uniqueOrderNo");
                    String status = json.getString("status");
                    String orderAmount = json.getString("orderAmount");
                    //查询订单
                    FkPayOrder payOrder = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, 4);
                    if (ObjectUtil.isNotNull(payOrder) && 1 != payOrder.getTradeStatus()) {
                        FkPayOrder.FkPayOrderBuilder orderBuilder = FkPayOrder.builder().orderId(orderId).tradeNo(outTradeNo).payWay(4);
                        //回调改成:处理中
                        fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(4).build());
                        BigDecimal fee = payOrder.getFee();
                        if (StrUtil.equals(Constants.SUCCESS, status) && ObjectUtil.equals(fee, new BigDecimal(orderAmount))) {
                            payFlag = fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(1).build());
                            if (payFlag > 0) {
                                String userName = payOrder.getUserName();
                                String payType = payConfig.getPayType();
                                fkOrderService.recharge(orderId, userName, "1", fee.toString(), payType);
                                result = Constants.SUCCESS;
                            }
                        } else {
                            payFlag = fkPayOrderService.updateFkPayOrder(orderBuilder.tradeStatus(2).build());
                        }
                    }
                }
                //告诉别再通知了
                response.getWriter().write(result);
            }
        } catch (Exception e) {
            log.error("yeePayNotify异常:" + e);
        } finally {
            log.info("yeePayNotify====>orderId:{}, payFlag:{}", orderId, payFlag);
        }
    }

    /**
     * 易宝清算结果通知
     * {
     * "feeMerchantNo": "10028123456",
     * "orderAmount": "608.26",
     * "csSuccessDate": "2022-02-17 15:57:43",
     * "orderId": "vcc125808844123456",
     * "merchantFee": "1.70",
     * "customerFee": "",
     * "parentMerchantNo": "10028123456",
     * "uniqueOrderNo": "1013202202170000003225123456",
     * "feeType": "PREPAID_REAL",
     * "ypSettleAmount": "608.26",
     * "merchantNo": "10028123456",
     * "status": "SUCCESS"
     * }
     *
     * @param request
     * @param response
     */
    @PostMapping("yeePay/divideApply")
    public void divideApply(HttpServletRequest request, HttpServletResponse response) {
        String result = Constants.FAIL;
        DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
        try {
            String appKey = request.getParameter("customerIdentification");
            String encrypt = request.getParameter("response");
            log.info("divideApply获取到的appKey为{},response为{}", appKey, encrypt);
            dto.setCipherText(encrypt);
            FkPayConfig payConfig = fkPayConfigService.selectFkPayConfigById(2L);
            if (ObjectUtil.isNotNull(payConfig)) {
                dto = DigitalEnvelopeUtils.decrypt(dto, YeePayUtil.getPrivateKey(payConfig.getPrivateKey()), YeePayUtil.getPubKey(payConfig.getPublicKey()));
                String plainText = dto.getPlainText();
                if (StringUtils.isNotBlank(plainText)) {
                    JSONObject json = JSONObject.parseObject(plainText);
                    log.info("易宝清算通知解析json:{}", json.toJSONString());
                    result = Constants.SUCCESS;
                    Map<String, Object> params = JSONObject.parseObject(json.toJSONString(), new TypeReference<Map<String, Object>>() {
                    });

                }
                //告诉别再通知了
                response.getWriter().write(result);
            }
        } catch (Exception e) {
            log.error("易宝清算通知异常:" + e);
        }
    }


}