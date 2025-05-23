package com.nova.login.sa.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.nova.common.core.model.result.ResResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wzh
 * @description: 用户Controller
 * @date: 2023/09/28 14:07
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 测试登录，浏览器访问：http://localhost:8080/api/user/login?username=wzh&password=123456
     * 登录成功后，Cookie可以从后端控制往浏览器中写入token值，会在前端每次发起请求时自动提交token值
     *
     * @param username username
     * @param password password
     */
    @RequestMapping("login")
    public ResResult<String> login(String username, String password) {
        // 第一步：比对前端提交的账号名称、密码
        if ("wzh".equals(username) && "123456".equals(password)) {

            Map<String, Object> map = new HashMap<>(16);
            map.put("userName", username);
            map.put("passWord", password);

            // 第二步：根据账号id，进行登录
            SaLoginParameter parameter = new SaLoginParameter();
            parameter.setExtra("userContext", map);
            StpUtil.login(10001, parameter);
            return ResResult.success("登录成功");
        }
        return ResResult.failure("登录失败");
    }

    @RequestMapping("getToken")
    public void getToken() {
        // 获取当前会话的 token 值
        String token = StpUtil.getTokenValue();
        System.err.println("token：" + token);

        // 获取当前`StpLogic`的 token 名称
        String tokenName = StpUtil.getTokenName();
        System.err.println("tokenName：" + tokenName);

        // 获取指定 token 对应的账号id，如果未登录，则返回 null
        Object loginIdByToken = StpUtil.getLoginIdByToken(token);
        System.err.println("loginIdByToken：" + loginIdByToken);

        // 获取当前会话剩余有效期（单位：s，返回-1代表永久有效）
        long tokenTimeout = StpUtil.getTokenTimeout();
        System.err.println("tokenTimeout：" + tokenTimeout);

        // 获取当前会话的 token 信息参数
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        System.err.println("tokenInfo：" + tokenInfo);
    }

    @RequestMapping("getLoginId")
    public void getLoginId() {
        // 获取当前会话账号id, 如果未登录，则抛出异常：`NotLoginException`
        Object loginId = StpUtil.getLoginId();
        System.err.println("loginId：" + loginId);

        // 获取当前会话账号id, 并转化为`String`类型
        String loginIdAsString = StpUtil.getLoginIdAsString();
        System.err.println("loginIdAsString：" + loginIdAsString);

        // 获取当前会话账号id, 并转化为`int`类型
        int loginIdAsInt = StpUtil.getLoginIdAsInt();
        System.err.println("loginIdAsInt：" + loginIdAsInt);

        // 获取当前会话账号id, 并转化为`long`类型
        long loginIdAsLong = StpUtil.getLoginIdAsLong();
        System.err.println("loginIdAsLong：" + loginIdAsLong);

        // ---------- 指定未登录情形下返回的默认值 ----------

        // 获取当前会话账号id, 如果未登录，则返回null
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        System.err.println("loginIdDefaultNull：" + loginIdDefaultNull);

    }

    /**
     * 注销登录
     */
    @RequestMapping("logout")
    public void logout() {
        // 当前会话注销登录
        StpUtil.logout();

        // 获取当前会话是否已经登录，返回true=已登录，false=未登录
        StpUtil.isLogin();

        // 检验当前会话是否已经登录, 如果未登录，则抛出异常：`NotLoginException`
        StpUtil.checkLogin();
    }

    /**
     * 其它接口
     */
    @RequestMapping("other")
    public void other() {
        Object loginId = StpUtil.getLoginId();
        System.err.println("登录会话账号：" + loginId);

        StpUtil.kickout(loginId);
        System.err.println("踢人下线：" + loginId);


    }

    // 登录校验：只有登录之后才能进入该方法
    @SaCheckLogin
    @RequestMapping("info")
    public ResResult<Object> info() {
        Object userContext = StpUtil.getExtra("userContext");
        return ResResult.success(userContext);
    }

    // 角色校验：必须具有指定角色才能进入该方法
    @SaCheckRole("super-admin")
    @RequestMapping("add")
    public String add() {
        return "用户增加";
    }

}
