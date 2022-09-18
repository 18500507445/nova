package com.nova.pay.strategy;

import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import org.springframework.stereotype.Component;

/**
 * @Description: 阿里支付实现类
 * @Author: wangzehui
 * @Date: 2022/9/18 11:37
 */
@Component
public class AliServiceImpl implements PayService {

    @Override
    public PayTypeEnum getPayType() {
        return PayTypeEnum.ALI;
    }

    @Override
    public AjaxResult pay(PayParam payParam) {
        return null;
    }
}
