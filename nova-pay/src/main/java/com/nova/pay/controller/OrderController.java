package com.nova.pay.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.OrderParam;
import com.nova.pay.entity.result.FkPayConfig;
import com.nova.pay.service.fk.FkOrderService;
import com.nova.pay.service.fk.FkPayConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 订单controller
 * @Author: wangzehui
 * @Date: 2022/8/23 17:28
 */
@RestController
@RequestMapping("/api/order/")
public class OrderController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private FkOrderService fkOrderService;

    @Autowired
    private FkPayConfigService fkPayConfigService;

    /**
     * 获取订单id
     */
    @PostMapping("getOrderId")
    @ResponseBody
    public AjaxResult getOrderId(OrderParam param) {
        Map<String, String> result = new HashMap<>(16);
        logger.info("getOrderId请求入参：{}", JSONUtil.toJsonStr(param));
        String sid = param.getSid();
        String source = param.getSource();
        String newVersion = param.getNewVersion();
        Integer payWay = param.getPayWay();
        String fromSource = param.getFromSource();
        String userName = param.getUserName();
        String totalAmount = param.getTotalAmount();
        String clientType = param.getClientType();
        if (ObjectUtil.hasEmpty(source, sid, clientType, userName, totalAmount, payWay)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        try {
            Map<String, String> paramsMap = new HashMap<>(16);
            paramsMap.put("function", "getOrderId");
            paramsMap.put("source", source);
            paramsMap.put("sid", sid);
            paramsMap.put("newVersion", newVersion);
            paramsMap.put("userName", userName);
            paramsMap.put("totalAmount", totalAmount);
            paramsMap.put("fromSource", fromSource);
            //根据clientType判断充值金币还是金豆
            String productId = sid + "|pay" + sid + "|" + clientType;
            paramsMap.put("productId", productId);
            paramsMap.put("tradeId", "");
            //随机一个支付配置
            FkPayConfig randomConfig = fkPayConfigService.getRandomConfigData(source, sid, String.valueOf(payWay));
            if (ObjectUtil.isNotNull(randomConfig)) {
                String payType = randomConfig.getPayType();
                paramsMap.put("payType", payType);
                //支付类型
                result.put("payType", payType);
                result.put("payConfigId", String.valueOf(randomConfig.getId()));
            }
            String orderId = fkOrderService.getOrderId(paramsMap);
            result.put("orderId", orderId);
            result.put("productId", productId);
        } catch (Exception e) {
            logger.error("getOrderId异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

}