package com.nova.log.config;

import cn.hutool.json.JSONUtil;
import com.nova.common.core.model.entity.LogEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: wzh
 * @description 日志拦截器
 * @date: 2023/08/01 16:15
 */
@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Resource
    private LogEntity logEntity;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logEntity.setParams(request.getParameterMap());
        logEntity.setPath(request.getServletPath());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("controller 请求日志：{}", JSONUtil.toJsonStr(logEntity));
    }
}
