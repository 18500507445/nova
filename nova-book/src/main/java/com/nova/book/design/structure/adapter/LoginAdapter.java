package com.nova.book.design.structure.adapter;

import cn.hutool.crypto.digest.DigestUtil;
import com.nova.book.design.structure.adapter.service.OtherLoginService;
import com.nova.book.design.structure.adapter.service.impl.UserServiceImpl;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description: 三方登录适配器
 * @date: 2023/10/25 17:36
 */
@Component
public class LoginAdapter extends UserServiceImpl implements OtherLoginService {

    /**
     * 继承老的用户登录方式，实现新的三方登录接口
     * ！！！先不考虑是否登陆过，没有查询逻辑哦
     */
    @Override
    public String wechatLogin(String token) {
        String result;
        //根据token换取三方的用户union_id，再根据不同的三方拼接自己的后缀生成userName
        String userName = token + "@wechat";
        String password = DigestUtil.md5Hex(userName);
        //父类注册
        super.register(userName, password);
        //父类登录
        result = super.login(userName, password);
        //返回结果
        return result;
    }
}
