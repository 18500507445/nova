package com.nova.pay.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.XmlConfig;
import com.nova.pay.config.AliPayConfig;
import com.nova.pay.config.WeChatConfig;
import com.nova.pay.config.YeePayConfig;
import com.nova.pay.utils.AliPayUtil;
import com.nova.pay.utils.YeePayUtil;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 支付通知controller
 * @Author: wangzehui
 * @Date: 2022/6/18 21:58
 */
@RestController
@RequestMapping("/api/payNotify/")
public class PayNotifyController {

    private static final Logger logger = LoggerFactory.getLogger(PayNotifyController.class);

    @Resource
    private AliPayUtil aliPayUtil;

    @Resource
    private WeChatConfig weChatConfig;

    /**
     * 支付宝通知
     * 流程
     * (1)先做幂等(查订单状态未支付的在做处理)，避免重复通知有垃圾数据
     * (2)然做修改支付订单状态为处理中，进行验签
     * (3)业务处理走消息队列
     */
    @PostMapping("aLiPay")
    public String aLiPay(HttpServletRequest request) {
        String orderId = "";
        String out = AliPayConfig.NOTIFY_FAIL;
        int payFlag = 0;
        String sign = "";
        //1.0 获取支付宝发送过来的信息
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
            orderId = MapUtil.getStr(params, "out_trade_no");

            String totalAmount = MapUtil.getStr(params, "total_amount");
            String tradeStatus = MapUtil.getStr(params, "trade_status");
            if (StringUtils.equals(AliPayConfig.TRADE_SUCCESS, tradeStatus)) {
                //内部订单id,支付宝交易流水号
                String outTradeNo = MapUtil.getStr(params, "trade_no");
                //回调改成:处理中
                //payService.update(new NtPayOrder(orderId, outTradeNo, 4, 1));

                String appId = MapUtil.getStr(params, "app_id");
                //NtPayConfig ntPayConfig = ntPayConfigService.selectAliNtPayConfigByAppId(appId);
                //String publicKey = ntPayConfig.getPublicKey();
                //String privateKey = ntPayConfig.getPrivateKey();
                //2.0 证书方式验签
                boolean check = aliPayUtil.rsaCheckV1(params, "publicKey");
                logger.info("支付宝通知参数:{}------验签结果:{}", JSONObject.toJSONString(params), check);
                if (check) {

                }
            }
        } catch (Exception e) {
            logger.error("aLiPayNotify异常:{}", e.getMessage());
        } finally {
            logger.info("aLiPayNotify====>orderId:{}, payFlag:{}, sign:{}", orderId, payFlag, sign);
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
        String sign = "";
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
            logger.info("weChatV2PayNotify：{}", JSONUtil.toJsonStr(notifyResult));
            orderId = notifyResult.getOutTradeNo();
            String tradeNo = notifyResult.getTransactionId();
            //查询订单
            if (ObjectUtil.isNotNull(null)) {
                //回调改成:处理中

                //查询配置参数，验签处理
                if (ObjectUtil.isNotNull(null)) {
                    WxPayService wxV2PayService = weChatConfig.getWxV2PayService("", "", "", "");
                    notifyResult = wxV2PayService.parseOrderNotifyResult(xmlString.toString());
                    if (StrUtil.equals(WxPayConstants.ResultCode.SUCCESS, notifyResult.getReturnCode())) {
                        //成功逻辑
                    } else {
                        //失败逻辑
                    }
                }
            }
            out = WxPayNotifyResponse.success("OK");
        } catch (IOException | WxPayException e) {
            String message = e.getMessage();
            out = WxPayNotifyResponse.success(message);
            logger.error("weChatV2PayNotify异常:{}", message);
        } finally {
            XmlConfig.fastMode = false;
            logger.info("weChatV2PayNotify====>orderId:{}, payFlag:{}, sign:{}", orderId, payFlag, sign);
        }
        return out;
    }


    /**
     * 苹果通知
     */
    @PostMapping("applePay")
    public String applePay(HttpServletRequest request) {
        String result = "ok";
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
            String orderId = MapUtil.getStr(params, "trade_no");
            String sign = MapUtil.getStr(params, "sign");
            //String version = getValue("version");
            params.put("version", "version");
            logger.info("苹果通知:{}", JSONObject.toJSONString(params));
            //3.0 通知修改成处理中,存入sign
            //payService.update(new NtPayOrder(orderId, sign, 4, 3, "false"));

            /**
             * 收到回调，延长过期时间
             */
            //int notifyOrderFlag = orderService.notifyOrder(orderId);
            //logger.info("applePay====>orderId:{}, notifyOrderFlag:{}", orderId, notifyOrderFlag);
            /**
             * 苹果支付回调修改成异步处理
             */
            //rabbitTemplate.convertAndSend(NtPayOrder.applePayNotifyListener, params);
        } catch (Exception e) {
            logger.error("苹果通知异常:" + e);
        }
        return result;
    }

    /**
     * 易宝支付结果通知
     * https://open.yeepay.com/docs/apis/ptssfk/jiaoyi/options__rest__v1.0__trade__order#anchor7
     * {"channelOrderId":"332022021622001422971445123456","orderId":"DQD620CUD57Q8A3123456","bankOrderId":"ST5207953516123456","paySuccessDate":"2022-02-16 20:26:05","channel":"ALIPAY","payWay":"MINI_PROGRAM","uniqueOrderNo":"1013202202160000003222123456","orderAmount":"2.00","payAmount":"2.00","payerInfo":"{\"bankCardNo\":\"\",\"bankId\":\"ALIPAY\",\"buyerLogonId\":\"136****1013\",\"mobilePhoneNo\":\"\",\"userID\":\"2088502619123456\"}","realPayAmount":"2.00","parentMerchantNo":"10085123456","merchantNo":"10085123456","status":"SUCCESS"}
     */
    @PostMapping("yeePay")
    public void yeePay(HttpServletRequest request, HttpServletResponse response) {
        String result = YeePayConfig.FAIL;
        DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
        try {
            String appKey = request.getParameter("customerIdentification");
            String encrypt = request.getParameter("response");
            logger.info("yeePay获取到的appKey为{},response为{}", appKey, encrypt);
            dto.setCipherText(encrypt);
            dto = DigitalEnvelopeUtils.decrypt(dto, YeePayUtil.getPrivateKey(YeePayConfig.PRIVATE_KEY), YeePayUtil.getPubKey(YeePayConfig.PUBLIC_KEY));
            String plainText = dto.getPlainText();
            if (StringUtils.isNotBlank(plainText)) {
                JSONObject json = JSONObject.parseObject(plainText);
                logger.info("易宝支付结果通知解析json:{}", json.toJSONString());
                result = YeePayConfig.SUCCESS;
                //易宝收款订单号
                String orderId = json.getString("orderId");
                String outTradeNo = json.getString("uniqueOrderNo");
                //payService.update(new NtPayOrder(orderId, outTradeNo, 4, 4));
                /**
                 * 收到回调，延长过期时间
                 */
                //int notifyOrderFlag = orderService.notifyOrder(orderId);
                //logger.info("applePay====>orderId:{}, notifyOrderFlag:{}", orderId, notifyOrderFlag);
                Map<String, Object> params = JSONObject.parseObject(json.toJSONString(), new TypeReference<Map<String, Object>>() {
                });
                //支付回调修改成异步处理
                //rabbitTemplate.convertAndSend(NtPayOrder.yeePayNotifyListener, params);
            }
            //告诉别再通知了
            response.getWriter().write(result);
        } catch (Exception e) {
            logger.error("易宝支付回调通知异常:" + e);
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
        String result = YeePayConfig.FAIL;
        DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
        try {
            String appKey = request.getParameter("customerIdentification");
            String encrypt = request.getParameter("response");
            logger.info("divideApply获取到的appKey为{},response为{}", appKey, encrypt);
            dto.setCipherText(encrypt);
            dto = DigitalEnvelopeUtils.decrypt(dto, YeePayUtil.getPrivateKey(YeePayConfig.PRIVATE_KEY), YeePayUtil.getPubKey(YeePayConfig.PUBLIC_KEY));
            String plainText = dto.getPlainText();
            if (StringUtils.isNotBlank(plainText)) {
                JSONObject json = JSONObject.parseObject(plainText);
                logger.info("易宝清算通知解析json:{}", json.toJSONString());
                result = YeePayConfig.SUCCESS;
                Map<String, Object> params = JSONObject.parseObject(json.toJSONString(), new TypeReference<Map<String, Object>>() {
                });
                //清算回调修改成异步处理
                //rabbitTemplate.convertAndSend(NtPayOrder.yeePayDivideApplyNotifyListener, params);
            }
            //告诉别再通知了
            response.getWriter().write(result);
        } catch (Exception e) {
            logger.error("易宝清算通知异常:" + e);
        }
    }

    /**
     * 用户开通易宝钱包通知
     */
    @PostMapping("/yeePay/wallet/notify")
    public void yeePayWallet(HttpServletRequest request, HttpServletResponse response) {
        String result = YeePayConfig.FAIL;
        DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
        int i = 0;
        try {
            String appKey = request.getParameter("customerIdentification");
            String encrypt = request.getParameter("response");
            logger.info("开通易宝钱包通知--appKey为{},response为{}", appKey, encrypt);
            dto.setCipherText(encrypt);
            dto = DigitalEnvelopeUtils.decrypt(dto, YeePayUtil.getPrivateKey(YeePayConfig.PRIVATE_KEY), YeePayUtil.getPubKey(YeePayConfig.PUBLIC_KEY));
            String plainText = dto.getPlainText();
            if (StringUtils.isNotBlank(plainText)) {
                JSONObject json = JSONObject.parseObject(plainText);
                logger.info("开通易宝钱包通知解析json:{}", json.toJSONString());
                String userId = json.getString("merchantUserNo");
                String walletId = json.getString("walletUserNo");
                if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(walletId)) {
                    //NtUserSetting param = new NtUserSetting();
                    //param.setUserId(Long.parseLong(userId));
                    //param.setWalletId(walletId);
                    //param.setWalletStatus(1);
                    //param.setCollectionStatus(1);
                    //i = ntUserSettingService.updateNtUserSetting(param);
                    if (i > 0) {
                        result = YeePayConfig.SUCCESS;
                    }
                }
            }
            response.getWriter().write(result);
        } catch (IOException e) {
            logger.error("yeePayWallet====>e:", e);
        }
    }

    /**
     * 商户入网通知地址
     * merchantNo:入网生成的易宝内部商户编号
     * auditOpinion:“申请已驳回”或者“申请已完成”时，回传的审核意见
     * applicationStatus:REVIEW_BACK(申请已驳回),AUTHENTICITY_VERIFYING(真实性验证中)，AGREEMENT_SIGNING(协议待签署),COMPLETED(申请已完成)
     * agreementSignUrl:当申请状态为“协议待签署”时，回调给商户该协议签署地址 备注：个人入网也用不到，一般直接就成功了，不需要签署协议
     */
    @PostMapping("/yeePay/account/notify")
    public void yeePayAccount(HttpServletRequest request, HttpServletResponse response) {
        String result = YeePayConfig.FAIL;
        DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
        try {
            String appKey = request.getParameter("customerIdentification");
            String encrypt = request.getParameter("response");
            logger.info("易宝小微入网通知--appKey为{},response为{}", appKey, encrypt);
            dto.setCipherText(encrypt);
            dto = DigitalEnvelopeUtils.decrypt(dto, YeePayUtil.getPrivateKey(YeePayConfig.PRIVATE_KEY), YeePayUtil.getPubKey(YeePayConfig.PUBLIC_KEY));
            String plainText = dto.getPlainText();
            if (StringUtils.isNotBlank(plainText)) {
                JSONObject json = JSONObject.parseObject(plainText);
                logger.info("易宝小微入网通知解析json:{}", json.toJSONString());
                String applicationNo = json.getString("applicationNo");
                if (StringUtils.isNotBlank(applicationNo)) {
                    //List<NtUserSetting> dataList = ntUserSettingService.selectNtUserSettingList(new NtUserSetting(applicationNo));
                    //if (CollectionUtils.isNotEmpty(dataList)) {
                    //    NtUserSetting data = dataList.get(0);
                    //    String merchantNo = json.getString("merchantNo");
                    //    String auditOpinion = json.getString("auditOpinion");
                    //    String applicationStatus = json.getString("applicationStatus");
                    //    data.setOpenAccountStatus(YeePayEnum.valuesOf(applicationStatus).getOpenAccountStatus());
                    //    data.setAuditReason(auditOpinion);
                    //    if (StringUtils.isNotBlank(merchantNo)) {
                    //        data.setMerchantNo(merchantNo);
                    //    }
                    //    //如果开通了钱包那么就不改变默认支付方式
                    //    if (1 != data.getCollectionStatus()) {
                    //        data.setCollectionStatus(2);
                    //    }
                    //    ntUserSettingService.update(data);
                    //}
                    result = YeePayConfig.SUCCESS;
                }
            }
            response.getWriter().write(result);
        } catch (Exception e) {
            logger.error("accountNotify====>e:", e);
        }
    }
}
