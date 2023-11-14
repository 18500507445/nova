package com.nova.tools.product.channel;

import com.nova.tools.product.manager.AbstractChannel;
import com.nova.tools.product.manager.ChannelFactory;
import com.nova.tools.product.manager.ChannelMessage;
import com.nova.tools.product.entity.ChannelConfig;
import com.nova.tools.product.enums.ChannelEnum;
import com.nova.tools.product.manager.ChannelInit;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: wzh
 * @description 严选渠道子类
 * @date: 2023/10/24 10:39
 */
@Component
public class YxChannel extends AbstractChannel implements ChannelMessage {

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
