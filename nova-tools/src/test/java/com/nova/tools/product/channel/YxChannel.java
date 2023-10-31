package com.nova.tools.product.channel;

import com.nova.tools.product.base.AbstractChannelBase;
import com.nova.tools.product.base.ChannelFactory;
import com.nova.tools.product.base.ChannelMessage;
import com.nova.tools.product.entity.ChannelConfig;
import com.nova.tools.product.enums.ChannelEnum;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wzh
 * @description 严选渠道子类
 * @date: 2023/10/24 10:39
 */
@Component
public class YxChannel extends AbstractChannelBase implements ChannelMessage {

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
    public String getMessagePool() {
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        ChannelFactory.add(getChannel(), this);
    }

    @Override
    public String productChange(HttpServletRequest request) {
        return null;
    }

    @Override
    public String priceChange(HttpServletRequest request) {
        return null;
    }

    @Override
    public String orderStatusChange(HttpServletRequest request) {
        return null;
    }

    @Override
    public String orderDeliveryChange(HttpServletRequest request) {
        return null;
    }

    @Override
    public String afterSaleStatusChange(HttpServletRequest request) {
        return null;
    }

    @Override
    public String messageChange() {
        return null;
    }
}
