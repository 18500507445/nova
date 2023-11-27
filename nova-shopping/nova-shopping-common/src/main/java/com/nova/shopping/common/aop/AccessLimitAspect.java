package com.nova.shopping.common.aop;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSONObject;
import com.nova.shopping.common.annotation.AccessLimit;
import com.nova.shopping.common.config.redis.RedisService;
import com.nova.shopping.common.constant.BaseController;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.common.constant.result.AjaxResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @description: redis计数器限流切入点
 * @author: wzh
 * @date: 2023/4/18 21:34
 */
@Component
@Aspect
public class AccessLimitAspect extends BaseController {

    @Resource
    private RedisService redisService;

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.nova.shopping.common.annotation.AccessLimit)"
            + "|| @within(com.nova.shopping.common.annotation.AccessLimit)")
    public void dsPointCut() {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 使用Java反射技术获取方法上是否有注解类
        MethodSignature signature = (MethodSignature) point.getSignature();
        AccessLimit accessLimit = signature.getMethod().getDeclaredAnnotation(AccessLimit.class);
        if (accessLimit == null) {
            // 正常执行方法
            return point.proceed();
        }
        int seconds = accessLimit.seconds();
        int maxCount = accessLimit.maxCount();
        String message = accessLimit.message();
        String ip = getIp();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes && StringUtils.isNotBlank(ip)) {
            HttpServletRequest request = attributes.getRequest();
            HttpServletResponse response = attributes.getResponse();
            String requestUrl = request.getRequestURI();

            String key = Constants.REDIS_KEY + "limit_userIp_" + ip;
            Long value = redisService.incr(key, 1L);
            redisService.expire(key, Convert.toLong(seconds));
            if (value > maxCount) {
                //超出访问次数，返回
                render(response, message);
            }
        }
        return point.proceed();
    }

    private void render(HttpServletResponse response, String message) throws Exception {
        if (null != response) {
            response.setContentType("application/json;charset=UTF-8");
            OutputStream out = response.getOutputStream();
            String str = JSONObject.toJSONString(AjaxResult.error(message));
            out.write(str.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        }
    }

}
