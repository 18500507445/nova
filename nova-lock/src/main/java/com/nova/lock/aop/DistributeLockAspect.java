package com.nova.lock.aop;

import com.nova.lock.annotation.Lock;
import com.nova.lock.core.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 分布式锁切面
 *
 * @author wangzehui
 * @date 2022/12/26 23:10
 */
@Slf4j
@Aspect
@Component
@Order(value = 2)
public class DistributeLockAspect {

    @Resource
    private RedissonLock redissonLock;

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.nova.lock.annotation.Lock)")
    private void pointcut() {

    }

    @Around("pointcut() && @annotation(lock)")
    public Object around(ProceedingJoinPoint point, Lock lock) {
        log.info("[开始]执行RedisLock环绕通知,获取Redis分布式锁开始");
        //获取锁名称
        String lockName = lock.value();
        //获取超时时间
        int expireSeconds = lock.expireSeconds();
        if (redissonLock.lock(lockName, expireSeconds)) {
            try {
                log.info("获取Redis分布式锁[成功]，加锁完成，开始执行业务逻辑...");
                return point.proceed();
            } catch (Throwable throwable) {
                log.error("获取Redis分布式锁[异常]，加锁失败", throwable);
                throwable.printStackTrace();
            } finally {
                redissonLock.release(lockName);
            }
            log.info("释放Redis分布式锁[成功]，解锁完成，结束业务逻辑...");
        } else {
            log.error("获取Redis分布式锁[失败]");
        }
        log.info("[结束]执行RedisLock环绕通知");
        return null;
    }


}
