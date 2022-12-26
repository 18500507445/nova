package com.nova.limit.aop;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.nova.limit.annotation.Lock;
import com.nova.limit.common.DistributeLockAspectUtil;
import com.nova.limit.common.DistributeLockException;
import com.nova.limit.common.LockConstant;
import com.nova.limit.core.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面
 *
 * @author wangzehui
 * @date 2022/12/26 23:10
 */
@Slf4j
@Aspect
@Component
@Order(value = 3)
public class DistributeLockAspect {

    @Resource
    private RedissonLock redissonLock;

    @Pointcut("@annotation(com.nova.limit.annotation.Lock)")
    private void pointcut() {
    }

    @Around("pointcut() && @annotation(lock)")
    public Object around(ProceedingJoinPoint point, Lock lock) throws Throwable {
        String key = DistributeLockAspectUtil.createLockKey(lock.value(), lock.keyPrefix(), point);
        // 非阻塞式，判断对当前key加锁是否成功，不成功直接抛出异常，比较适合管理后台保存数据重复提交的场景
        if (!lock.isBlock() && !redissonLock.tryLock(key, TimeUnit.MILLISECONDS, 0)) {
            throw new DistributeLockException(StrUtil.isNotEmpty(lock.lockFailMsg())
                    ? lock.lockFailMsg() : LockConstant.OPERATE_QUICK);
        }
        // 阻塞式，没设置过期时间，判断在waitTime时间内是否加锁成功，加锁成功后，会根据业务执行的时间锁自动续期
        if (ObjectUtil.equal(lock.expireTime(), -1L)) {
            if (!redissonLock.tryLock(key, TimeUnit.MILLISECONDS, lock.waitTime())) {
                throw new DistributeLockException(StrUtil.isNotEmpty(lock.lockFailMsg())
                        ? lock.lockFailMsg() : LockConstant.OPERATE_QUICK);
            }
        } else {
            // 阻塞式，设置了过期时间，判断在waitTime时间内是否加锁成功，加锁成功后，锁在expireTime时间自动过期，不在自动续期
            if (!redissonLock.tryLock(key, TimeUnit.MILLISECONDS, lock.waitTime(), lock.expireTime())) {
                throw new DistributeLockException(StrUtil.isNotEmpty(lock.lockFailMsg())
                        ? lock.lockFailMsg() : LockConstant.OPERATE_QUICK);
            }
        }
        log.info("加锁成功！key:{}", key);
        try {
            return point.proceed();
        } finally {
            redissonLock.unlock(key);
        }
    }


}
