package com.nova.login.justauth.controller;

import cn.hutool.json.JSONUtil;
import com.nova.common.core.controller.BaseController;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/2 21:07
 * @see <a href="https://xkcoding.com/2019/05/22/spring-boot-login-with-oauth.html">快速集成第三方登录功能</a>
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/oauth")
public class OauthController extends BaseController {

    private final AuthRequestFactory factory;

    /**
     * 类型列表
     *
     * @see <a href="http://localhost:8080/api/oauth">类型列表</a>
     */
    @GetMapping
    public List<String> loginType() {
        return factory.oauthList();
    }

    /**
     * 登录
     *
     * @param type     第三方登录类型
     * @param response response
     * @throws IOException
     */
    @RequestMapping("/login/{type}")
    public void login(@PathVariable String type, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(type);
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * 登录成功后的回调
     *
     * @param type     第三方登录类型
     * @param callback 携带返回的信息
     * @return 登录成功后的信息
     */
    @RequestMapping("/callback/{type}")
    public AuthResponse callback(@PathVariable String type, AuthCallback callback) {
        AuthRequest authRequest = factory.get(type);
        AuthResponse response = authRequest.login(callback);
        log.info("【response】= {}", JSONUtil.toJsonStr(response));
        return response;
    }
}
