package com.nova.tools.product;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wzh
 * @description 京东渠道子类
 * @date: 2023/10/24 10:34
 */
@Component
public class JdChannel extends AbstractChannelBase {

    @Override
    public ChannelEnum getChannel() {
        return ChannelEnum.JD;
    }

    @Override
    public ChannelConfig getChannelConfig() {
        return null;
    }

    @Override
    public String getToken() {
        return this.getChannel().getChannelName() + "_token";
    }

    @Override
    public void afterPropertiesSet() {
        ChannelFactory.add(getChannel(), this);
    }

    @Override
    public String refreshToken() {
        return this.getChannel().getChannelName() + "_refreshToken";
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
    public String productNotify(HttpServletRequest request) {
        return null;
    }

    @Override
    public String priceNotify(HttpServletRequest request) {
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
    public String orderStatusNotify(HttpServletRequest request) {
        return null;
    }

    @Override
    public String orderDeliveryNotify(HttpServletRequest request) {
        return null;
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
    public String afterSaleStatusNotify(HttpServletRequest request) {
        return null;
    }

    @Override
    public String getMessagePool() {
        return null;
    }

}
