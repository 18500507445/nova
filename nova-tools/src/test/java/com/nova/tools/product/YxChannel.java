package com.nova.tools.product;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wzh
 * @description 严选渠道子类
 * @date: 2023/10/24 10:39
 */
@Component
public class YxChannel extends AbstractChannelBase {

    @Override
    public ChannelEnum getChannel() {
        return ChannelEnum.YX;
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
    public String getBrand() {
        return null;
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public String getGoodsList() {
        return null;
    }

    @Override
    public String getGoodsDetail() {
        return null;
    }

    @Override
    public String goodsNotify(HttpServletRequest request) {
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

    @Override
    public void afterPropertiesSet() {
        ChannelFactory.add(getChannel(), this);
    }

}
