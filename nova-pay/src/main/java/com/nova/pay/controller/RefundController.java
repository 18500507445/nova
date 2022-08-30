package com.nova.pay.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 退款controller
 * @Author: wangzehui
 * @Date: 2022/6/18 21:59
 */
@RestController
@RequestMapping("/api/refund/")
public class RefundController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RefundController.class);

    /**
     * 微信v2退款
     */
    @PostMapping("weChatV2Pay")
    @ResponseBody
    public AjaxResult weChatV2Pay() {

        return AjaxResult.success();
    }

    /**
     * 微信v3退款
     */
    @PostMapping("weChatV3Pay")
    @ResponseBody
    public AjaxResult weChatV3Pay() {

        return AjaxResult.success();
    }

    /**
     * 支付宝退款
     */
    @PostMapping("aLiPay")
    @ResponseBody
    public AjaxResult aLiPay() {
        return AjaxResult.success();
    }
}
