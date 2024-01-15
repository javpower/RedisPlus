package com.gc.easy.redis.impl;

import com.gc.easy.redis.model.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

/**
 * @Auther: gc.x
 * @Date: 2019/5/11 0011 14:14
 * @Description:
 */
public class WriteLock extends AbstractLock {

    public WriteLock(RedissonClient redissonClient, LockInfo lockInfo) {
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }

    @Override
    protected RLock getLock(String name) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(name);
        RLock rLock = readWriteLock.writeLock();
        return rLock;
    }
}
