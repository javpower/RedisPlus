package com.gc.easy.redis.impl;

import com.gc.easy.redis.ILock;
import com.gc.easy.redis.model.LockInfo;
import lombok.Data;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: gc.x
 * @Date: 2019/5/11 0011 13:51
 * @Description:
 */
@Data
public abstract class AbstractLock implements ILock {
    protected RLock rLock;

    protected LockInfo lockInfo;

    protected RedissonClient redissonClient;

    @Override
    public boolean acquire() {
        try {
            // 1. 最常见的使用方法
            // lock.lock();
            // 2. 支持过期解锁功能,xxx秒钟以后自动解锁, 无需调用unlock方法手动解锁
            // lock.lock(xx, TimeUnit.SECONDS);
            // 3. 尝试加锁，最多等待xxx秒，上锁以后10秒自动解锁
            // lock.lock(xx,xx, TimeUnit.SECONDS);
            rLock = getLock(lockInfo.getName());
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        //查询当前线程是否保持此锁定
        if (rLock.isHeldByCurrentThread()) {
            rLock.unlockAsync();
        }
    }

    /**
     * 获取锁
     *
     * @param name 锁名称
     * @return
     */
    protected abstract RLock getLock(String name);
}
