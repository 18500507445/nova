package com.nova.shopping.pay.service.strategy.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.nova.shopping.common.config.redis.RedisService;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.common.constant.result.AjaxResult;
import com.nova.shopping.common.enums.PayWayEnum;
import com.nova.shopping.pay.entity.MyPayConfig;
import com.nova.shopping.pay.entity.MyPayOrder;
import com.nova.shopping.pay.entity.param.PayParam;
import com.nova.shopping.pay.payment.open.GooglePayment;
import com.nova.shopping.pay.service.pay.MyPayConfigService;
import com.nova.shopping.pay.service.pay.MyPayOrderService;
import com.nova.shopping.pay.service.strategy.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @description: 谷歌支付实现类
 * @author: wzh
 * @date: 2023/3/18 21:13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleServiceImpl implements PayService {

    private final MyPayOrderService myPayOrderService;

    private final MyPayConfigService myPayConfigService;

    private final RedisService redisService;

    private final GooglePayment googlePayment;

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.GOOGLE_PAY;
    }

    @Override
    public AjaxResult pay(PayParam param) {
        //3app支付
        int type = Integer.parseInt(param.getType());
        String source = param.getSource();
        String sid = param.getSid();
        Long payConfigId = param.getPayConfigId();
        int businessCode = param.getBusinessCode();
        String orderId = param.getOrderId();
        String totalAmount = param.getTotalAmount();
        String productId = param.getProductId();
        String userName = param.getUserName();
        String currencyType = param.getCurrencyType();
        if (ObjectUtil.hasEmpty(source, sid, businessCode, orderId, payConfigId, type, userName, totalAmount, productId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        try {
            //查询订单
            MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 5);
            if (ObjectUtil.isNull(payOrder)) {
                MyPayOrder insert = MyPayOrder.builder().source(source)
                        .sid(sid)
                        .orderId(orderId)
                        .productId(productId)
                        .payConfigId(payConfigId)
                        .userName(userName)
                        .tradeStatus(0)
                        .payWay(5)
                        .type(type)
                        .businessCode(businessCode)
                        .currencyType(currencyType)
                        .fee(new BigDecimal(totalAmount)).build();
                int flag = myPayOrderService.insertMyPayOrder(insert);
                if (0 == flag) {
                    return AjaxResult.error("1000", "创建支付订单失败,请从新下单");
                }
            } else {
                if (0 != payOrder.getTradeStatus()) {
                    return AjaxResult.error("1000", "支付结果确认中，请稍候");
                }
            }
        } catch (Exception e) {
            log.error("googlePay异常：{}", e.getMessage());
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
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult getOpenId(PayParam param) {
        Long payConfigId = param.getPayConfigId();
        //获取支付配置
        MyPayConfig payConfig = myPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        Map<String, String> tokenMap = googlePayment.getAccessToken(payConfig.getAppId(), payConfig.getAppSecret(), payConfig.getPaySecret(), "1");
        if (MapUtil.isNotEmpty(tokenMap)) {
            String accessToken = MapUtil.getStr(tokenMap, "accessToken");
            String key = Constants.REDIS_KEY + "getAccessToken_" + payConfig.getId();
            redisService.set(key, accessToken, MapUtil.getLong(tokenMap, "expiresIn") - 500);
            String refreshToken = MapUtil.getStr(tokenMap, "refreshToken");
            if (StrUtil.isNotBlank(refreshToken)) {
                payConfig.setPaySecret(refreshToken);
                myPayConfigService.updateMyPayConfig(payConfig);
            }
        }
        return AjaxResult.success("接口待开发。。。");
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
