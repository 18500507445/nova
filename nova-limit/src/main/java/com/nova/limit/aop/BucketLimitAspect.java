package com.nova.limit.aop;

import com.alibaba.fastjson2.JSON;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.limit.annotation.BucketLimit;
import com.nova.limit.bucket.Bucket;
import com.nova.limit.bucket.RedisTokenBucket;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @description: 令牌桶限流切入点
 * @author: wzh
 * @date: 2023/4/18 21:34
 */
@Component
@Aspect
public class BucketLimitAspect extends BaseController {

    @Resource
    private RedisTokenBucket tokenBucket;

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.nova.limit.annotation.BucketLimit)"
            + "|| @within(com.nova.limit.annotation.BucketLimit)")
    public void dsPointCut() {

    }

    @Around("dsPointCut()")
    public Object before(ProceedingJoinPoint point) throws Throwable {
        // 使用Java反射技术获取方法上是否有注解类
        MethodSignature signature = (MethodSignature) point.getSignature();
        BucketLimit accessLimit = signature.getMethod().getDeclaredAnnotation(BucketLimit.class);
        if (accessLimit == null) {
            // 正常执行方法
            return point.proceed();
        }
        int seconds = accessLimit.seconds();
        int maxCount = accessLimit.maxCount();
        String message = accessLimit.message();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            HttpServletResponse response = attributes.getResponse();
            String requestUrl = request.getRequestURI();

            Bucket bucket = new Bucket().setPutSpeed(seconds).setMaxCapacity(maxCount).setKey(requestUrl);
            tokenBucket.createTokenBucket(bucket);
            if (!tokenBucket.getToken(requestUrl)) {
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
            String str = JSON.toJSONString(AjaxResult.error(message));
            out.write(str.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        }
    }

}
