package com.nova.limit.interceptor;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.nova.common.core.domain.AjaxResult;
import com.nova.limit.annotation.AccessLimit;
import com.nova.redis.core.RedisService;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 限流
 * @Author: wangzehui
 * @Date: 2022/11/19 16:34
 */
@Component
public class LimitInterceptor implements HandlerInterceptor {

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
            String key = request.getRequestURI();
            //如果需要登录
            if (login) {
                //获取登录的session进行判断 这里假设用户是1,项目中是动态获取的userId
                key += "" + "1";
            }

            Cache<String, Object> cache = CacheBuilder.newBuilder()
                    //设置缓存最大容量
                    .maximumSize(100)
                    //过期策略，写入失效时间
                    .expireAfterWrite(seconds, TimeUnit.MINUTES)
                    .build();

            Object value = cache.getIfPresent(key);
            if (ObjectUtil.isNotNull(value)) {
                int count = Convert.toInt(value);
                if (count > maxCount) {
                    //超出访问次数，返回
                    render(response);
                    return false;
                } else {
                    count += 1;
                    cache.put(key, count);
                }
            } else {
                cache.put(key, 1);
            }
        }
        return true;

    }

    private void render(HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(AjaxResult.error("超出访问次数，已被限流"));
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
