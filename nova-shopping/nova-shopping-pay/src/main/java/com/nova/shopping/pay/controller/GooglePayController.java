package com.nova.shopping.pay.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.nova.shopping.common.constant.dto.OrderParam;
import com.nova.shopping.common.constant.result.AjaxResult;
import com.nova.shopping.pay.payment.open.GooglePayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2023/3/7 19:24
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google/")
public class GooglePayController {

    private final GooglePayment googlePayment;

    /**
     * 谷歌校验
     */
    @PostMapping("verify")
    public AjaxResult verify(OrderParam param) {
        Map<String, Object> result = new HashMap<>(16);
        log.info("verify请求入参：{}", JSONUtil.toJsonStr(param));
        String packageName = param.getPackageName();
        String applicationName = param.getApplicationName();
        String productId = param.getProductId();
        String purchaseToken = param.getPurchaseToken();
        String keyPath = param.getKeyPath();
        if (ObjectUtil.hasEmpty(packageName, applicationName, productId, purchaseToken, keyPath)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        try {
            ProductPurchase verify = googlePayment.verify(packageName, applicationName, productId, purchaseToken, keyPath, false);
            if (ObjectUtil.isNotNull(verify)) {
                result.put("purchaseState", verify.getPurchaseState());
                result.put("orderId", verify.getOrderId());
            }
        } catch (Exception e) {
            log.error("verify异常：{}", e.getMessage());
        } finally {
            log.info("verify返回结果：{}", JSONUtil.toJsonStr(result));
        }
        return AjaxResult.success(result);
    }
}
