package com.nova.pay.service.pay.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.nova.cache.redis.RedisService;
import com.nova.common.constant.Constants;
import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.HuaweiPayParam;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.entity.result.FkPayConfig;
import com.nova.pay.entity.result.FkPayOrder;
import com.nova.pay.enums.PayWayEnum;
import com.nova.pay.payment.open.HuaweiPayment;
import com.nova.pay.service.fk.FkPayConfigService;
import com.nova.pay.service.fk.FkPayOrderService;
import com.nova.pay.service.pay.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description: 华为支付实现类
 * @Author: wangzehui
 * @Date: 2022/12/2 10:19
 */
@Slf4j
@Service
public class HuaweiServiceImpl implements PayService {

    @Resource
    private RedisService redisService;

    @Resource
    private HuaweiPayment huaweiPayment;

    @Autowired
    private FkPayOrderService fkPayOrderService;

    @Autowired
    private FkPayConfigService fkPayConfigService;

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.HUA_WEI_PAY;
    }

    @Override
    public AjaxResult pay(PayParam param) {
        String source = param.getSource();
        String sid = param.getSid();
        int type = Integer.parseInt(param.getType());
        Long payConfigId = param.getPayConfigId();
        int businessCode = param.getBusinessCode();
        String orderId = param.getOrderId();
        String totalAmount = param.getTotalAmount();
        String productId = param.getProductId();
        String userName = param.getUserName();
        if (ObjectUtil.hasEmpty(source, sid, businessCode, orderId, payConfigId, type, userName, totalAmount, productId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        try {
            //查询订单
            FkPayOrder payOrder = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, 7);
            if (ObjectUtil.isNull(payOrder)) {
                FkPayOrder insert = FkPayOrder.builder().source(source)
                        .sid(sid)
                        .orderId(orderId)
                        .productId(productId)
                        .payConfigId(payConfigId)
                        .userName(userName)
                        .tradeStatus(0)
                        .payWay(7)
                        .type(type)
                        .businessCode(businessCode)
                        .fee(new BigDecimal(totalAmount)).build();
                int flag = fkPayOrderService.insertFkPayOrder(insert);
                if (0 == flag) {
                    return AjaxResult.error("1000", "创建支付订单失败,请从新下单");
                }
            } else {
                if (0 != payOrder.getTradeStatus()) {
                    return AjaxResult.error("1000", "支付结果确认中，请稍候");
                }
            }
        } catch (Exception e) {
            log.error("huaweiPay异常：{}", e.getMessage());
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult refund(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }

    @Override
    public AjaxResult queryOrder(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }

    /**
     * 获取accessToken
     * @param param
     * @return
     */
    @Override
    public AjaxResult getOpenId(PayParam param) {
        Object accessToken = "";
        Long payConfigId = param.getPayConfigId();
        if (ObjectUtil.hasEmpty(payConfigId)) {
            return AjaxResult.error("1000", "缺少必要参数payConfigId");
        }
        //获取支付配置
        FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        String key = Constants.REDIS_KEY + "getAccessToken_" + payConfig.getId();
        Object o = redisService.get(key);
        if (ObjectUtil.isNotNull(o)) {
            accessToken = o.toString();
        } else {
            HuaweiPayParam data = HuaweiPayParam.builder()
                    .clientId(payConfig.getAppId())
                    .clientSecret(payConfig.getAppSecret())
                    .build();
            Map<String, Object> tokenMap = huaweiPayment.getAccessToken(data);
            if (MapUtil.isNotEmpty(tokenMap)) {
                accessToken = MapUtil.getStr(tokenMap, "access_token");
                if (ObjectUtil.isNotNull(accessToken)) {
                    redisService.set(key, accessToken, MapUtil.getLong(tokenMap, "expires_in") - 600);
                }
            }
        }
        return AjaxResult.success(accessToken);
    }

    @Override
    public AjaxResult closeOrder(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }

    @Override
    public AjaxResult merchantTransfer(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }
}
