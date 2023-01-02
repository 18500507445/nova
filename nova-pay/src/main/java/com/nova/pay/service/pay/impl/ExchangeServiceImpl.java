package com.nova.pay.service.pay.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.enums.PayWayEnum;
import com.nova.pay.service.fk.FkOrderService;
import com.nova.pay.service.pay.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 金币兑换实现类
 * @author: wzh
 * @date: 2022/9/27 14:27
 */
@Service
public class ExchangeServiceImpl implements PayService {

    @Autowired
    private FkOrderService fkOrderService;

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.EXCHANGE;
    }

    /**
     * 金币兑换
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult pay(PayParam param) {
        String source = param.getSource();
        String sid = param.getSid();
        String totalAmount = param.getTotalAmount();
        String userName = param.getUserName();
        if (ObjectUtil.hasEmpty(source, sid, userName, totalAmount)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        Map<String, String> map = new HashMap<>(16);
        map.put("function", "ayoiRedeemNow");
        map.put("user_name", userName);
        map.put("exchange_amount", totalAmount);
        Map<String, String> exchangeMap = fkOrderService.getCommon(map);
        return AjaxResult.success(MapUtil.getStr(exchangeMap, "code"), MapUtil.getStr(exchangeMap, "msg"), MapUtil.getStr(exchangeMap, "data"));
    }

    @Override
    public AjaxResult refund(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }

    @Override
    public AjaxResult queryOrder(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }

    @Override
    public AjaxResult getOpenId(PayParam param) {
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
