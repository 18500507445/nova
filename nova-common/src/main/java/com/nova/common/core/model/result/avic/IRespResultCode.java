package com.nova.common.core.model.result.avic;

/**
 * 返回编码基础，所有自定义编码枚举实现此接口，供APIException使用
 *
 * @author suo
 * @date 2023/8/23 21:15
 */
public interface IRespResultCode {

    String getBizCode();

    String getBizMessage();

}
