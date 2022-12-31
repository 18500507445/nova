package com.nova.pay.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.OrderParam;
import com.nova.pay.payment.open.GooglePayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2022/11/7 19:24
 */
@Slf4j
@RestController
@RequestMapping("/api/google/")
public class GooglePayController {

    @Resource
    private GooglePayment googlePayment;

    /**
     * 谷歌校验
     */
    @PostMapping("verify")
    @ResponseBody
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
