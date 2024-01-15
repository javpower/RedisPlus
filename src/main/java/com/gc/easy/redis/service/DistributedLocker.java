package com.gc.easy.redis.service;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: gc.x
 * @Date: 2020/8/6 15:03
 * @Description:
 */
public interface DistributedLocker {
    RLock lock(String lockKey);

    RLock lock(String lockKey, long timeout);

    RLock lock(String lockKey, TimeUnit unit, long timeout);

    boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

    void release(String lockKey);
}
