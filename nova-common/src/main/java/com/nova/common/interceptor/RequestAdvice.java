package com.nova.common.interceptor;

import cn.hutool.json.JSONUtil;
import com.nova.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @description: 请求拦截器 (@RequestBody的请求生效) 该接口主要是用于给请求体参数做前后增强处理的
 * @author: wzh
 * @date: 2022/8/30 15:29
 */
@Slf4j
@ControllerAdvice
public class RequestAdvice implements RequestBodyAdvice {

    /**
     * 该方法用于判断当前请求，是否要执行beforeBodyRead方法
     * methodParameter方法的参数对象
     * type方法的参数类型
     * aClass 将会使用到的Http消息转换器类类型
     * 注意：此判断方法，会在beforeBodyRead 和 afterBodyRead方法前都触发一次。
     *
     * @return 返回true则会执行beforeBodyRead
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("进入RequestAdvice-supports:" + Constants.IS_OPEN);
        return Constants.IS_OPEN;
    }

    /**
     * 在Http消息转换器执转换，之前执行
     * inputMessage 客户端的请求数据
     * parameter方法的参数对象
     * targetType方法的参数类型
     * converterType 将会使用到的Http消息转换器类类型
     *
     * @return 返回 一个自定义的HttpInputMessage
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        log.info("进入beforeBodyRead");

        // 常见的业务仓场景有: 1 记录日志 2 内容加密解密 3 是否开启分页功能
        log.info("拦截到的请求参数为 = {}", parameter.toString());
        Method method = parameter.getMethod();
        log.info("请求体读取前={}==>{}==>{}==>{}", method.getDeclaringClass().getSimpleName(), method.getName(), targetType.toString(), converterType.getName());

        //类路径
        String[] classPath = parameter.getContainingClass().getAnnotation(RequestMapping.class).value();
        //方法路径
        String[] methodPath = parameter.getMethodAnnotation(RequestMapping.class).value();
        if (inputMessage.getBody().available() <= 0) {
            return inputMessage;
        }

        return inputMessage;
    }

    /**
     * 在Http消息转换器执转换，之后执行
     * body 转换后的对象
     * inputMessage 客户端的请求数据
     * parameter handler方法的参数类型
     * targetType handler方法的参数类型
     * converterType 使用的Http消息转换器类类型
     *
     * @return 返回一个新的对象
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = parameter.getMethod();
        log.info("进入afterBodyRead");
        log.info("{}.{}:{}", method.getDeclaringClass().getSimpleName(), method.getName(), JSONUtil.toJsonStr(body));
        return body;
    }

    /**
     * 参数与afterBodyRead相同，不过这个方法处理的是，body为空的情况
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = parameter.getMethod();
        log.info("进入handleEmptyBody");
        log.info("{}.{}:{}", method.getDeclaringClass().getSimpleName(), method.getName(), JSONUtil.toJsonStr(body));
        return body;
    }
}
