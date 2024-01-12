package com.nova.tools.product.base;

import cn.hutool.core.util.StrUtil;
import com.nova.tools.product.entity.ChannelConfig;
import com.nova.tools.product.enums.ChannelEnum;
import org.springframework.beans.factory.InitializingBean;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wzh
 * @description 渠道抽象基类，实现Spring的InitializingBean，各个渠道子类重写方法放入容器
 * 设计思路，各个渠道都有的，抽取成抽象方法，子类必须实现，如果拿不准的，抽取成普通方法，求同存异
 * @date: 2023/10/24 10:25
 */
public abstract class AbstractChannel implements InitializingBean {

    //异常提示消息
    public final String EXCEPTION_MESSAGE = StrUtil.indexedFormat("子类：[{0}] 没有此方法，按需手动复写", this.getClass().getSimpleName());

    // ---------------------------------基础功能---------------------------------

    /**
     * 获取当前渠道
     */
    public abstract ChannelEnum getChannel();

    /**
     * 获取渠道配置
     */
    public abstract ChannelConfig getConfig();

    // ---------------------------------令牌Api---------------------------------

    /**
     * 获取渠道访问令牌
     */
    public abstract <T> String getToken(T t, T... args);

    /**
     * 刷新令牌
     */
    public String refreshToken() {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    // ---------------------------------商品Api---------------------------------

    /**
     * 获取品牌信息
     */
    public abstract String getBrand();

    /**
     * 获取分类信息
     */
    public abstract String getCategory();

    /**
     * 获取商品列表（sku、spu）
     */
    public abstract String getProductList();

    /**
     * 获取商品详情
     */
    public abstract String getProductDetail();

    /**
     * 获取商品价格
     */
    public String getPrice() {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    /**
     * 获取商品图片
     */
    public String getPicture() {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    // ---------------------------------订单Api---------------------------------

    /**
     * 提交订单
     */
    public abstract String createOrder();

    /**
     * 取消订单
     */
    public abstract String cancelOrder();

    /**
     * 区域可售，下单库存
     */
    public abstract boolean isAreaSell();

    // ---------------------------------物流Api---------------------------------

    /**
     * 订单物流
     */
    public abstract String orderLogistics();

    /**
     * 物流状态变更通知
     */
    public abstract String logisticsStatusNotify(HttpServletRequest request);

    // ---------------------------------售后Api---------------------------------

    /**
     * 提交售后单
     */
    public abstract String createAfterSaleOrder();


    // ---------------------------------地址Api（预留）---------------------------------

    /**
     * 获取省份，一级地址
     */
    public String getProvince() {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    /**
     * 获取城市，二级地址
     */
    public String getCity() {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    /**
     * 获取区/县，三级地址
     */
    public String getArea() {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    /**
     * 获取镇/街道，四级地址
     */
    public String getTown() {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

}
