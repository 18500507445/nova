package com.nova.limit.aop;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.limit.annotation.BucketLimit;
import com.nova.limit.utils.JedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 令牌桶Lua脚本限流切入点
 * @author: wzh
 * @date: 2023/4/18 21:34
 */
@Component
@Aspect
public class BucketLimitAspect extends BaseController implements InitializingBean {

    @Resource
    private JedisUtil jedisUtil;

    private String scriptLua;

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.nova.limit.annotation.BucketLimit)"
            + "|| @within(com.nova.limit.annotation.BucketLimit)")
    public void dsPointCut() {

    }

    @Around("dsPointCut()")
    public Object before(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        BucketLimit accessLimit = signature.getMethod().getDeclaredAnnotation(BucketLimit.class);
        if (accessLimit == null) {
            return point.proceed();
        }
        int rate = accessLimit.rate();
        int maxCount = accessLimit.maxCount();
        int requestNum = accessLimit.requestNum();
        String message = accessLimit.message();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes && StringUtils.isNotBlank(scriptLua)) {
            HttpServletRequest request = attributes.getRequest();
            HttpServletResponse response = attributes.getResponse();
            String requestUrl = request.getRequestURI();
            final long evalsha = jedisUtil.evalsha(scriptLua, getKeys(requestUrl), Arrays.asList(String.valueOf(rate), String.valueOf(maxCount), String.valueOf(requestNum)));
            System.out.println(DateUtil.now() + "，evalsha = " + evalsha);
            if (evalsha < 1) {
                //超出访问次数，返回
                render(response, message);
            }
        }
        return point.proceed();
    }

    public List<String> getKeys(String key) {
        String prefix = "request_rate_limiter:" + key;
        String tokenKey = prefix + ":tokens";
        String timestampKey = prefix + ":timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

    private void render(HttpServletResponse response, String message) throws Exception {
        if (null != response) {
            response.setContentType("application/json;charset=UTF-8");
            OutputStream out = response.getOutputStream();
            String str = JSON.toJSONString(AjaxResult.error(message));
            out.write(str.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("bucket.lua");
        byte[] buffer = new byte[(int) classPathResource.getFile().length()];
        classPathResource.getInputStream().read(buffer);
        scriptLua = jedisUtil.scriptLoad(buffer);
    }
}