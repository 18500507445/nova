package com.nova.tools.product.channel;

import com.nova.tools.product.base.AbstractChannelBase;
import com.nova.tools.product.base.ChannelFactory;
import com.nova.tools.product.base.ChannelMessage;
import com.nova.tools.product.entity.ChannelConfig;
import com.nova.tools.product.enums.ChannelEnum;
import com.nova.tools.product.init.CommonInit;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: wzh
 * @description 京东渠道子类
 * @date: 2023/10/24 10:34
 */
@Component
public class JdChannel extends AbstractChannelBase implements ChannelMessage {

    @Override
    public ChannelEnum getChannel() {
        return ChannelEnum.JD;
    }

    @Override
    public void afterPropertiesSet() {
        ChannelFactory.add(getChannel(), this);
    }

    @Override
    public ChannelConfig getConfig() {
        return CommonInit.CONFIG_MAP.get(getChannel().getName());
    }

    @Override
    public <T> String getToken(T t, T... args) {
        return this.getChannel().getName() + "_token";
    }

    @Override
    public String refreshToken() {
        return this.getChannel().getName() + "_refreshToken";
    }

    @Override
    public String getBrand() {
        return null;
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public String getProductList() {
        return null;
    }

    @Override
    public String getProductDetail() {
        return null;
    }

    @Override
    public String createOrder() {
        return null;
    }

    @Override
    public String cancelOrder() {
        return null;
    }

    @Override
    public boolean isAreaSell() {
        return false;
    }

    @Override
    public String orderLogistics() {
        return null;
    }

    @Override
    public String logisticsStatusNotify(HttpServletRequest request) {
        return null;
    }

    @Override
    public String createAfterSaleOrder() {
        return null;
    }

    @Override
    public String productChange(Map<String, Object> params) {
        return null;
    }

    @Override
    public String priceChange(Map<String, Object> params) {
        return null;
    }

    @Override
    public String orderStatusChange(Map<String, Object> params) {
        return null;
    }

    @Override
    public String orderDeliveryChange(Map<String, Object> params) {
        return null;
    }

    @Override
    public String afterSaleStatusChange(Map<String, Object> params) {
        return null;
    }

    @Override
    public String addressChange(Map<String, Object> params) {
        return null;
    }

}
