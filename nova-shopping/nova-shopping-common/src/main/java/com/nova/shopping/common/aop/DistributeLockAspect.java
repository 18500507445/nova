package com.nova.shopping.common.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.nova.shopping.common.annotation.Lock;
import com.nova.shopping.common.config.redisson.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wzh
 * @description: 分布式锁切面
 * @date 2023/03/26 23:10
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
    @Pointcut("@annotation(com.nova.shopping.common.annotation.Lock)")
    private void pointcut() {

    }

    @Around("pointcut() && @annotation(lock)")
    public Object around(ProceedingJoinPoint point, Lock lock) {
        final TimeInterval timer = DateUtil.timer();
        //获取锁名称
        String lockName = lock.value();
        //获取超时时间
        int expireSeconds = lock.expireSeconds();
        if (redissonLock.lock(lockName, expireSeconds)) {
            try {
                return point.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                redissonLock.release(lockName);
                log.info("释放Redis分布式锁[成功]，解锁完成，耗时：{} ms", timer.interval());
            }
        } else {
            log.error("获取Redis分布式锁[失败]");
        }
        return null;
    }


}
