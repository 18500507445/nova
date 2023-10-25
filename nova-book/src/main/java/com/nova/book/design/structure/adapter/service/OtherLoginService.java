package com.nova.book.design.structure.adapter.service;

/**
 * @author: wzh
 * @description 三方登录接口
 * @date: 2023/10/25 17:32
 */
public interface OtherLoginService {

    /**
     * 微信登录
     * 用户授权获取code，h5根据code获取token，请求接口带入token获取用户信息，三方union_id
     */
    String wechatLogin(String token);
}
