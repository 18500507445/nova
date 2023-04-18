package com.nova.limit.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.nova.common.constant.Constants;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.limit.annotation.AccessLimit;
import com.nova.limit.utils.JedisUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @description: 限流拦截器
 * @author: wzh
 * @date: 2022/11/19 16:34
 */
@Component
public class LimitInterceptor extends BaseController implements HandlerInterceptor {

    @Resource
    private JedisUtil jedisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            //获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean login = accessLimit.needLogin();
            String message = accessLimit.message();
            String requestUrl = request.getRequestURI();
            String ip = getIp();
            if (StrUtil.isNotBlank(ip)) {
                String key = Constants.REDIS_KEY + "limit_userIp_" + ip;
                long value = jedisUtil.incr(key);
                jedisUtil.expire(key, seconds);
                if (value > maxCount) {
                    //超出访问次数，返回
                    render(response, message);
                    return false;
                }
            }
        }
        return true;

    }

    private void render(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(AjaxResult.error(message));
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
