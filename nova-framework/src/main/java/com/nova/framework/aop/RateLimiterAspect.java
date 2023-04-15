package com.nova.framework.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.nova.common.annotation.ExtRateLimiter;
import com.nova.framework.config.ThreadPoolConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/13 20:30
 */

@Aspect
@Component
public class RateLimiterAspect {

    /**
     * 存放接口是否已经存在
     */
    private static final ConcurrentHashMap<String, RateLimiter> RATE_LIMITER_MAP = new ConcurrentHashMap<>();

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.nova.common.annotation.ExtRateLimiter)"
            + "|| @within(com.nova.common.annotation.ExtRateLimiter)")
    public void rlAop() {

    }

    @Around("rlAop()")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 使用Java反射技术获取方法上是否有@ExtRateLimiter注解类
        ExtRateLimiter extRateLimiter = signature.getMethod().getDeclaredAnnotation(ExtRateLimiter.class);
        if (extRateLimiter == null) {
            // 正常执行方法
            return proceedingJoinPoint.proceed();
        }
        // ############获取注解上的参数 配置固定速率 ###############
        // 获取配置的速率
        double value = extRateLimiter.value();
        // 获取等待令牌等待时间
        long timeOut = extRateLimiter.timeOut();
        RateLimiter rateLimiter = getRateLimiter(value, timeOut);
        // 判断令牌桶获取token 是否超时
        boolean tryAcquire = rateLimiter.tryAcquire(timeOut, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            serviceDown();
            return null;
        }
        // 获取到令牌,直接执行..
        return proceedingJoinPoint.proceed();

    }

    /**
     * 获取RateLimiter对象
     *
     * @param value
     * @param timeOut
     * @return
     */
    private RateLimiter getRateLimiter(double value, long timeOut) {
        // 获取当前URL
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestURI = request.getRequestURI();
        RateLimiter rateLimiter = null;
        if (!RATE_LIMITER_MAP.containsKey(requestURI)) {
            // 开启令牌通限流 独立线程
            rateLimiter = RateLimiter.create(value);
            RATE_LIMITER_MAP.put(requestURI, rateLimiter);
        } else {
            rateLimiter = RATE_LIMITER_MAP.get(requestURI);
        }
        return rateLimiter;
    }

    /**
     * 服务降级
     *
     * @throws IOException
     */
    private void serviceDown() throws IOException {
        // 执行服务降级处理
        System.out.println("执行降级方法,亲,服务器忙！请稍后重试!");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try {
            writer.println("执行降级方法,亲,服务器忙！请稍后重试!");
        } catch (Exception e) {

        } finally {
            writer.close();
        }

    }

    public static void main(String[] args) {
        // 使用Java反射技术获取方法上是否有@ExtRateLimiter注解类
        ExtRateLimiter extRateLimiter = ThreadPoolConfig.class.getClass().getAnnotation(ExtRateLimiter.class);
        System.out.println(extRateLimiter);
    }

}
