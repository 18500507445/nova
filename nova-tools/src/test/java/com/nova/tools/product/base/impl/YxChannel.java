package com.nova.tools.product.base.impl;

import com.nova.tools.product.entity.ChannelConfig;
import com.nova.tools.product.enums.ChannelEnum;
import com.nova.tools.product.base.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wzh
 * @description 严选渠道子类
 * @date: 2023/10/24 10:39
 */
@Component
public final class YxChannel extends AbstractChannel implements ChannelConvert, ChannelMessage {

    @Override
    public ChannelEnum getChannel() {
        return ChannelEnum.YX;
    }

    @Override
    public void afterPropertiesSet() {
        ChannelFactory.add(getChannel(), this);
    }

    @Override
    public ChannelConfig getConfig() {
        return ChannelInit.CONFIG_MAP.get(getChannel().getName());
    }

    @Override
    public <T> String getToken(T t, T... args) {
        return this.getChannel().getName() + "_token";
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
    public <T> void productMessage(T t) {

    }

    @Override
    public <T> void orderMessage(T t) {

    }

    @Override
    public <T> void logisticsMessage(T t) {

    }

    @Override
    public <T> void afterSaleMessage(T t) {

    }

    @Override
    public <T> Object brandConvert(Object context, T value) {
        return null;
    }

    @Override
    public <T> Object categoryConvert(Object context, T value) {
        return null;
    }

    @Override
    public <T> Object skuConvert(Object context, T value) {
        return null;
    }

    @Override
    public <T> Object pictureConvert(Object context, T value) {
        return null;
    }

    @Override
    public <T> Object attrConvert(Object context, T value) {
        return null;
    }

    @Override
    public <T> Object stockConvert(Object context, T value) {
        return null;
    }

    @Override
    public <T> Object taxConvert(Object context, T value) {
        return null;
    }

    @Override
    public <T> Object specConvert(Object context, T value) {
        return null;
    }

    @Override
    public <T> Object areaConvert(Object context, T value) {
        return null;
    }
}
