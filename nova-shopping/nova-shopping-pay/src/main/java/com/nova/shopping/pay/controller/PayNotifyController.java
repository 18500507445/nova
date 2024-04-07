package com.nova.shopping.pay.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.XmlConfig;
import com.nova.shopping.common.constant.BaseController;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.common.constant.result.AjaxResult;
import com.nova.shopping.pay.entity.MyPayConfig;
import com.nova.shopping.pay.entity.MyPayOrder;
import com.nova.shopping.pay.entity.param.HuaweiPayParam;
import com.nova.shopping.pay.entity.param.KsPayParam;
import com.nova.shopping.pay.entity.param.PayParam;
import com.nova.shopping.pay.payment.open.*;
import com.nova.shopping.pay.service.pay.MyPayConfigService;
import com.nova.shopping.pay.service.pay.MyPayOrderService;
import com.nova.shopping.pay.service.strategy.impl.HuaweiServiceImpl;
import com.nova.shopping.pay.service.strategy.impl.KsPayServiceImpl;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.yeepay.shade.org.apache.commons.collections4.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 * @description: 支付通知controller
 * @author: wzh
 * @date: 2023/3/21 20:57
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payNotify/")
public class PayNotifyController extends BaseController {

    private final WeChatPayment weChatPayment;

    private final AliPayment aliPayment;

    private final ApplePayment applePayment;

    private final KsPayment ksPayment;

    private final HuaweiPayment huaweiPayment;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final MyPayOrderService myPayOrderService;

    //private final MyOrderService myOrderService;

    private final MyPayConfigService myPayConfigService;

    private final KsPayServiceImpl ksPayService;

    private final HuaweiServiceImpl huaweiService;

    /**
     * 支付宝通知
     */
    @PostMapping("aLiPay")
    public String aLiPay(HttpServletRequest request) {
        String orderId = "";
        String out = AliPayment.NOTIFY_FAIL;
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
            MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 1);
            if (ObjectUtil.isNotNull(payOrder) && 1 != payOrder.getTradeStatus()) {
                MyPayOrder.MyPayOrderBuilder orderBuilder = MyPayOrder.builder().orderId(orderId).tradeNo(tradeNo).payWay(1);
                //回调改成:处理中
                myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(4).build());
                //查询配置参数，验签处理
                MyPayConfig payConfig = myPayConfigService.getConfigData(payOrder.getPayConfigId());
                boolean check = aliPayment.rsaCheckV1(params, payConfig.getPublicKey());
                log.info("aLiPayNotify通知参数:{},验签结果:{}", JSONUtil.toJsonStr(params), check);
                BigDecimal fee = payOrder.getFee();
                String userName = payOrder.getUserName();
                if (check) {
                    if (StringUtils.equals(AliPayment.TRADE_SUCCESS, tradeStatus) && ObjectUtil.equals(fee, new BigDecimal(totalAmount))) {
                        payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(1).build());
                        if (payFlag > 0) {
                            //myOrderService.successOrderHandler(payOrder.getSource(), payOrder.getSid(), payOrder.getBusinessCode(), orderId, userName, "1", fee.toString());
                            out = AliPayment.NOTIFY_SUCCESS;
                        }
                    } else {
                        payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(2).build());
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
            log.info("weChatV2PayNotify:{}", JSONUtil.toJsonStr(notifyResult));
            orderId = notifyResult.getOutTradeNo();
            String tradeNo = notifyResult.getTransactionId();
            //查询订单
            MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 2);
            if (ObjectUtil.isNotNull(payOrder)) {
                MyPayOrder.MyPayOrderBuilder orderBuilder = MyPayOrder.builder().orderId(orderId).tradeNo(tradeNo).payWay(2);
                //回调改成:处理中
                myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(4).build());
                //查询配置参数，验签处理
                MyPayConfig payConfig = myPayConfigService.getConfigData(payOrder.getPayConfigId());
                if (ObjectUtil.isNotNull(payConfig) && 1 != payOrder.getTradeStatus()) {
                    WxPayService wxV2PayService = weChatPayment.getWxV2PayService(payConfig.getAppId(), payConfig.getMchId(), payConfig.getPaySecret(), payConfig.getKeyPath());
                    notifyResult = wxV2PayService.parseOrderNotifyResult(xmlString.toString());
                    BigDecimal fee = payOrder.getFee();
                    String userName = payOrder.getUserName();
                    if (StrUtil.equals(WxPayConstants.ResultCode.SUCCESS, notifyResult.getReturnCode())) {
                        payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(1).build());
                        if (payFlag > 0) {
                            //myOrderService.successOrderHandler(payOrder.getSource(), payOrder.getSid(), payOrder.getBusinessCode(), orderId, userName, "1", fee.toString());
                        }
                    } else {
                        payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(2).build());
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
        TimeInterval timer = DateUtil.timer();
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
            log.info("applePayNotify:{}", JSONUtil.toJsonStr(params));
            //查询订单
            MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 3);
            if (ObjectUtil.isNotNull(payOrder) && 1 != payOrder.getTradeStatus()) {
                MyPayOrder.MyPayOrderBuilder orderBuilder = MyPayOrder.builder().orderId(orderId).payWay(3).sign(sign);
                //回调改成:处理中
                myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(4).remark("false").build());
                //查询配置参数，验签处理
                MyPayConfig payConfig = myPayConfigService.getConfigData(payOrder.getPayConfigId());
                //2.0 然后进行验证
                Map<String, Object> verify = applePayment.verify(sign, sid, version);
                String transactionId = MapUtil.getStr(verify, "transaction_id");
                String status = MapUtil.getStr(verify, "status");
                boolean check = ArrayUtils.contains(new String[]{"0"}, status) && !StringUtils.equals(Constants.ZERO, transactionId);
                log.info("applePayNotify验签结果====>orderId:{}, verify:{}, check:{}", orderId, verify, check);
                if (check && ObjectUtil.isEmpty(payOrder.getTradeNo())) {
                    //3.0 通知修改成处理中,存入sign、验签结果
                    payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(1).tradeNo(transactionId).remark("ture").build());
                    if (payFlag > 0) {
                        //myOrderService.successOrderHandler(payOrder.getSource(), payOrder.getSid(), payOrder.getBusinessCode(), orderId, payOrder.getUserName(), "1", payOrder.getFee().toString());
                    }
                } else {
                    payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(2).build());
                }
            }
        } catch (Exception e) {
            log.error("applePayNotify异常:{}", e.getMessage());
        } finally {
            log.info("applePayNotify====>orderId:{}, payFlag:{}, 耗时:{}毫秒", orderId, payFlag, timer.interval());
        }
        return AjaxResult.success(result);
    }

    /**
     * 一级易宝支付结果通知
     * https://open.yeepay.com/docs/apis/ptssmy/jiaoyi/options__rest__v1.0__trade__order#anchor7
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
            log.info("yeePayNotify====>appKey:{},response:{}", appKey, encrypt);
            dto.setCipherText(encrypt);
            MyPayConfig payConfig = myPayConfigService.selectMyPayConfigById(2L);
            if (ObjectUtil.isNotNull(payConfig)) {
                dto = DigitalEnvelopeUtils.decrypt(dto, YeePayment.getPrivateKey(payConfig.getPrivateKey()), YeePayment.getPubKey(payConfig.getPublicKey()));
                String plainText = dto.getPlainText();
                if (StringUtils.isNotBlank(plainText)) {
                    JSONObject json = JSONUtil.parseObj(plainText);
                    log.info("易宝支付结果通知解析json:{}", json.toString());
                    //易宝收款订单号
                    orderId = json.getStr("orderId");
                    String outTradeNo = json.getStr("uniqueOrderNo");
                    String status = json.getStr("status");
                    String orderAmount = json.getStr("orderAmount");
                    //查询订单
                    MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 4);
                    if (ObjectUtil.isNotNull(payOrder) && 1 != payOrder.getTradeStatus()) {
                        MyPayOrder.MyPayOrderBuilder orderBuilder = MyPayOrder.builder().orderId(orderId).tradeNo(outTradeNo).payWay(4);
                        //回调改成:处理中
                        myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(4).build());
                        BigDecimal fee = payOrder.getFee();
                        if (StrUtil.equals(Constants.SUCCESS, status) && ObjectUtil.equals(fee, new BigDecimal(orderAmount))) {
                            payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(1).build());
                            if (payFlag > 0) {
                                //myOrderService.successOrderHandler(payOrder.getSource(), payOrder.getSid(), payOrder.getBusinessCode(), orderId, payOrder.getUserName(), "1", fee.toString());
                                result = Constants.SUCCESS;
                            }
                        } else {
                            payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(2).build());
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
            log.info("divideApplyNotify====>appKey:{},response:{}", appKey, encrypt);
            dto.setCipherText(encrypt);
            MyPayConfig payConfig = myPayConfigService.selectMyPayConfigById(2L);
            if (ObjectUtil.isNotNull(payConfig)) {
                dto = DigitalEnvelopeUtils.decrypt(dto, YeePayment.getPrivateKey(payConfig.getPrivateKey()), YeePayment.getPubKey(payConfig.getPublicKey()));
                String plainText = dto.getPlainText();
                if (StringUtils.isNotBlank(plainText)) {
                    JSONObject json = JSONUtil.parseObj(plainText);
                    log.info("易宝清算通知解析json:{}", json);
                    result = Constants.SUCCESS;
                    Map<String, Object> params = json.toBean(new TypeReference<Map<String, Object>>() {

                    });

                }
                //告诉别再通知了
                response.getWriter().write(result);
            }
        } catch (Exception e) {
            log.error("易宝清算通知异常:" + e);
        }
    }


    /**
     * 谷歌支付通知
     */
    @PostMapping("googlePay")
    public AjaxResult googlePay(HttpServletRequest request) {
        TimeInterval timer = DateUtil.timer();
        int payFlag = 0;
        String orderId = "";
        Map<String, String> params = new HashMap<>(16);
        try {
            //1.0 解析谷歌通知请求信息
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String str : requestParams.keySet()) {
                String[] values = requestParams.get(str);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(str, new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }
            String original = MapUtil.getStr(params, "originalJson");
            if (ObjectUtil.hasNull(original)) {
                return AjaxResult.error("1000", "missing required parameter:originalJson");
            }
            JSONObject originalJson = JSONUtil.parseObj(original);
            String packageName = originalJson.getStr("packageName", "");
            orderId = originalJson.getStr("obfuscatedProfileId", "");
            String tradeNo = originalJson.getStr("orderId", "");
            String productId = originalJson.getStr("productId", "");
            String purchaseToken = originalJson.getStr("purchaseToken", "");
            log.info("googlePay通知:====>originalJson:{},orderId:{},tradeNo:{},productId:{},packageName:{},purchaseToken:{}", original, orderId, tradeNo, productId, packageName, purchaseToken);
            //查询订单
            MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 5);
            if (ObjectUtil.hasNull(payOrder, packageName, orderId, tradeNo, productId, purchaseToken)) {
                return AjaxResult.error("1000", "missing required parameter");
            }
            if (1 != payOrder.getTradeStatus()) {
                MyPayOrder.MyPayOrderBuilder orderBuilder = MyPayOrder.builder().orderId(orderId).tradeNo(tradeNo).payWay(5);
                //回调改成:处理中
                myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(4).build());
                //查询配置参数，验签处理
                MyPayConfig payConfig = myPayConfigService.getConfigData(payOrder.getPayConfigId());
                //2.0 然后进行验证
                Map<String, Object> map = new HashMap<>(16);
                map.put("packageName", packageName);
                map.put("applicationName", payConfig.getMchId());
                map.put("productId", productId);
                map.put("purchaseToken", purchaseToken);
                map.put("keyPath", payConfig.getKeyPath());
                String purchase = HttpUtil.createPost(Constants.GOOGLE_VERIFY_URL).form(map).execute().body();
                log.info("googlePayNotify验签结果====>purchase:{}", JSONUtil.toJsonStr(purchase));
                if (ObjectUtil.isNotNull(purchase)) {
                    JSONObject jsonObject = JSONUtil.parseObj(purchase);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data.containsKey("purchaseState") && 0 == data.getInt("purchaseState") && data.containsKey("orderId") && StringUtils.equals(tradeNo, data.getStr("orderId"))) {
                        payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(1).remark("ture").build());
                        if (payFlag > 0) {
                            //myOrderService.successOrderHandler(payOrder.getSource(), payOrder.getSid(),payOrder.getBusinessCode(), orderId, payOrder.getUserName(), "1", payOrder.getFee().toString());
                            return AjaxResult.success("pay success");
                        }
                    } else {
                        return AjaxResult.error("google orderId status error or orderId error");
                    }
                } else {
                    return AjaxResult.error("verify order result is null");
                }
            } else {
                return AjaxResult.error("google order status error ");
            }
        } catch (Exception e) {
            log.error("googlePayNotify异常:{}", e.getMessage());
        } finally {
            log.info("googlePayNotify====>orderId:{}, payFlag:{}, 耗时:{}毫秒", orderId, payFlag, timer.interval());
        }
        return AjaxResult.error("fail");
    }


    /**
     * 快手小程序支付回调通知
     */
    @PostMapping("ksPay")
    public JSONObject ksPay(@RequestBody Object body, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TimeInterval timer = DateUtil.timer();
        int payFlag = 0;
        String orderId = "";
        try {
            //1.0 处理快手通知过来的请求信息
            String ksSign = request.getHeader("kwaisign");
            JSONObject jsonObject = JSONUtil.parseObj(body, JSONConfig.create().setIgnoreNullValue(false));
            String jsonStr = JSONUtil.toJsonStr(jsonObject);
            log.info("ksPayNotify====>jsonStr:{}, ksSign:{}", jsonStr, ksSign);
            //2.0 验签
            if (ObjectUtil.isAllNotEmpty(ksSign, jsonStr)) {
                JSONObject data = jsonObject.getJSONObject("data");
                orderId = data.getStr("out_order_no");
                //订单支付状态 PROCESSING-处理中｜SUCCESS-成功｜FAILED-失败
                String status = data.getStr("status");
                //快手小程序平台订单号
                String ksOrderNo = data.getStr("ks_order_no");
                //交易编号
                String tradeNo = data.getStr("trade_no");
                //3.0 查询订单 查询配置参数 验签
                MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 6);
                if (ObjectUtil.isNotNull(payOrder) && 1 != payOrder.getTradeStatus()) {
                    MyPayConfig payConfig = myPayConfigService.getConfigData(payOrder.getPayConfigId());
                    boolean check = StrUtil.equals(ksSign, DigestUtils.md5Hex(jsonStr + payConfig.getAppSecret()));
                    MyPayOrder.MyPayOrderBuilder orderBuilder = MyPayOrder.builder().orderId(orderId).payWay(6).sign(ksOrderNo);
                    //回调改成:处理中 存入ksOrderNo
                    myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(4).build());
                    if (check && StrUtil.equals(Constants.SUCCESS, status)) {
                        //4.0 修改状态、加款
                        payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(1).tradeNo(tradeNo).build());
                        if (payFlag > 0) {
                            result.set("result", 1);
                            result.set("message_id", jsonObject.getStr("message_id"));
                            //myOrderService.successOrderHandler(payOrder.getSource(), payOrder.getSid(), payOrder.getBusinessCode(), orderId, payOrder.getUserName(), "1", payOrder.getFee().toString());
                            //5.0 同步快手订单，后续结算需要
                            AjaxResult accessTokenResult = ksPayService.getOpenId(PayParam.builder().payConfigId(payOrder.getPayConfigId()).build());
                            if (ObjectUtil.isNotNull(accessTokenResult)) {
                                JSONObject accessTokenJson = JSONUtil.parseObj(accessTokenResult);
                                if (accessTokenJson.containsKey("data") && StrUtil.isNotBlank(accessTokenJson.getStr("data"))) {
                                    String finalOrderId = orderId;
                                    threadPoolTaskExecutor.execute(() -> {
                                        KsPayParam report = KsPayParam.builder()
                                                .appId(payConfig.getAppId())
                                                .accessToken(accessTokenJson.getStr("data"))
                                                .outOrderNo(finalOrderId)
                                                .openId(payOrder.getOperator())
                                                .build();
                                        Map<String, Object> reportMap = ksPayment.reportOrder(report);
                                        log.debug("ksPayNotifyReportMap====>:{}", JSONUtil.toJsonStr(reportMap));
                                    });
                                }
                            }
                        }
                    } else {
                        payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(2).build());
                    }
                }
            }
        } catch (Exception e) {
            log.error("ksPayNotify异常:{}", e.getMessage());
        } finally {
            log.info("ksPayNotify====>orderId:{}, payFlag:{}, 耗时:{}毫秒", orderId, payFlag, timer.interval());
        }
        return result;
    }


    /**
     * 快手小程序结算回调通知
     */
    @PostMapping("ksPaySettle")
    public JSONObject ksPaySettle(@RequestBody Object body, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TimeInterval timer = DateUtil.timer();
        int updateFlag = 0;
        String orderId = "";
        try {
            String ksSign = request.getHeader("kwaisign");
            JSONObject jsonObject = JSONUtil.parseObj(body, JSONConfig.create().setIgnoreNullValue(false));
            String jsonStr = JSONUtil.toJsonStr(jsonObject);
            log.info("ksPaySettleNotify====>jsonStr:{}, ksSign:{}", jsonStr, ksSign);
            if (ObjectUtil.isAllNotEmpty(ksSign, jsonStr)) {
                JSONObject data = jsonObject.getJSONObject("data");
                /**
                 * 外部结算单号，即开发者结算请求的单号 job里传入的结算单号和体育的订单号一致
                 * {@link com.my.pay.job.PayOrderJob#ksSettleOrderJob}
                 */
                orderId = data.getStr("out_settle_no");
                String status = data.getStr("status");
                MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 6);
                if (ObjectUtil.isNotNull(payOrder)) {
                    MyPayConfig payConfig = myPayConfigService.getConfigData(payOrder.getPayConfigId());
                    boolean check = StrUtil.equals(ksSign, DigestUtils.md5Hex(jsonStr + payConfig.getAppSecret()));
                    if (check && StrUtil.equals(Constants.SUCCESS, status) && ObjectUtil.equals(1, payOrder.getTradeStatus())) {
                        updateFlag = myPayOrderService.updateMyPayOrder(MyPayOrder.builder().orderId(orderId).remark("已结算").build());
                        if (updateFlag > 0) {
                            result.set("result", 1);
                            result.set("message_id", jsonObject.getStr("message_id"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("ksPaySettleNotify异常:{}", e.getMessage());
        } finally {
            log.info("ksPaySettleNotify====>orderId:{}, updateFlag:{}, 耗时:{}毫秒", orderId, updateFlag, timer.interval());
        }
        return result;
    }

    /**
     * 快手小程序退款回调通知
     */
    @PostMapping("ksRefund")
    public JSONObject ksRefund(@RequestBody Object body, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TimeInterval timer = DateUtil.timer();
        int updateFlag = 0;
        String orderId = "";
        try {
            String ksSign = request.getHeader("kwaisign");
            JSONObject jsonObject = JSONUtil.parseObj(body, JSONConfig.create().setIgnoreNullValue(false));
            String jsonStr = JSONUtil.toJsonStr(jsonObject);
            log.info("ksRefundNotify====>jsonStr:{}, ksSign:{}", jsonStr, ksSign);
            if (ObjectUtil.isAllNotEmpty(ksSign, jsonStr)) {
                JSONObject data = jsonObject.getJSONObject("data");
                orderId = data.getStr("out_refund_no");
                String status = data.getStr("status");
                MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 6);
                if (ObjectUtil.isNotNull(payOrder)) {
                    MyPayConfig payConfig = myPayConfigService.getConfigData(payOrder.getPayConfigId());
                    boolean check = StrUtil.equals(ksSign, DigestUtils.md5Hex(jsonStr + payConfig.getAppSecret()));
                    if (check && StrUtil.equals(Constants.SUCCESS, status) && ObjectUtil.equals(1, payOrder.getTradeStatus())) {
                        updateFlag = myPayOrderService.updateMyPayOrder(MyPayOrder.builder().tradeStatus(3).orderId(orderId).remark("已退款").build());
                        if (updateFlag > 0) {
                            result.set("result", 1);
                            result.set("message_id", jsonObject.getStr("message_id"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("ksRefundNotify异常:{}", e.getMessage());
        } finally {
            log.info("ksRefundNotify====>orderId:{}, updateFlag:{}, 耗时:{}毫秒", orderId, updateFlag, timer.interval());
        }
        return result;
    }

    /**
     * 华为支付通知
     */
    @PostMapping("huaweiPay")
    public AjaxResult huaweiPay(HttpServletRequest request) {
        TimeInterval timer = DateUtil.timer();
        int payFlag = 0;
        String orderId = "";
        Map<String, String> params = new HashMap<>(16);
        try {
            //1.0 解析华为通知请求信息
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String str : requestParams.keySet()) {
                String[] values = requestParams.get(str);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(str, URLDecoder.decode(new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            }
            log.info("huaweiPayNotify====>jsonStr:{}", JSONUtil.toJsonStr(params));
            String consumePurchaseData = MapUtil.getStr(params, "consumePurchaseData");
            String dataSignature = MapUtil.getStr(params, "dataSignature");
            if (ObjectUtil.isAllNotEmpty(consumePurchaseData, dataSignature)) {
                JSONObject content = JSONUtil.parseObj(consumePurchaseData);
                orderId = content.getStr("developerPayload");
                String purchaseToken = content.getStr("purchaseToken");
                String productId = content.getStr("productId");
                String tradeNo = content.getStr("payOrderId");
                //2.0 查询订单
                MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 7);
                if (ObjectUtil.isNotNull(payOrder) && 1 != payOrder.getTradeStatus()) {
                    MyPayOrder.MyPayOrderBuilder orderBuilder = MyPayOrder.builder().orderId(orderId).payWay(6);

                    //3.0 rsa验签后 回调改成:处理中
                    myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(4).build());

                    //4.0 查询配置参数、查询凭证、验证购买
                    MyPayConfig payConfig = myPayConfigService.getConfigData(payOrder.getPayConfigId());
                    AjaxResult accessTokenResult = huaweiService.getOpenId(PayParam.builder().payConfigId(payOrder.getPayConfigId()).build());
                    if (ObjectUtil.isNotNull(accessTokenResult)) {
                        JSONObject accessTokenJson = JSONUtil.parseObj(accessTokenResult);
                        if (accessTokenJson.containsKey("data") && StrUtil.isNotBlank(accessTokenJson.getStr("data"))) {
                            String verify = huaweiPayment.verify(HuaweiPayParam.builder().productId(productId).purchaseToken(purchaseToken).accessToken(accessTokenJson.getStr("data")).build());
                            log.info("huaweiPayNotify验签结果====>verify:{}", JSONUtil.toJsonStr(verify));
                            if (ObjectUtil.isNotNull(verify)) {
                                JSONObject verifyJsonObject = JSONUtil.parseObj(verify);
                                if (StrUtil.equals(Constants.ZERO, verifyJsonObject.getStr("responseCode"))) {
                                    payFlag = myPayOrderService.updateMyPayOrder(orderBuilder.tradeStatus(1).tradeNo(tradeNo).remark("ture").build());
                                    if (payFlag > 0) {
                                        //myOrderService.successOrderHandler(payOrder.getSource(), payOrder.getSid(),payOrder.getBusinessCode(), orderId, payOrder.getUserName(), "1", payOrder.getFee().toString());
                                        return AjaxResult.success("pay success");
                                    }
                                } else {
                                    return AjaxResult.error("huawei orderId status error or orderId error");
                                }
                            } else {
                                return AjaxResult.error("verify order result is null");
                            }
                        }
                    }
                } else {
                    return AjaxResult.error("order status error or order not found");
                }
            }
            return AjaxResult.success("pay success");
        } catch (Exception e) {
            log.error("huaweiPayPayNotify异常:{}", e.getMessage());
        } finally {
            log.info("huaweiPayNotify====>orderId:{}, payFlag:{}, 耗时:{}毫秒", orderId, payFlag, timer.interval());
        }
        return AjaxResult.error("fail");
    }

}