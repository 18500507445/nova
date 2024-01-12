package com.nova.tools.product.base;


/**
 * @author: wzh
 * @description 渠道数据转换接口
 * <p>
 * （1）渠道apiResponse --> context
 * （2）渠道apiResponse + context --> context（多次包装context）
 * @date: 2023/11/16 17:55
 */
public interface ChannelConvert {

    /**
     * 品牌转换
     */
    <T> Object brandConvert(Object context, T value);

    /**
     * 分类转换
     */
    <T> Object categoryConvert(Object context, T value);

    /**
     * sku转换
     */
    <T> Object skuConvert(Object context, T value);

    /**
     * sku图片转换
     */
    <T> Object pictureConvert(Object context, T value);

    /**
     * 商品属性转换
     */
    <T> Object attrConvert(Object context, T value);

    /**
     * 商品库存转换
     */
    <T> Object stockConvert(Object context, T value);

    /**
     * 商品税码转换
     */
    <T> Object taxConvert(Object context, T value);

    /**
     * 商品详情转换
     */
    <T> Object specConvert(Object context, T value);

    /**
     * 区域转换（预留的）
     */
    <T> Object areaConvert(Object context, T value);

}
