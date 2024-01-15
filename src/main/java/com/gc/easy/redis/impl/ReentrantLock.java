package com.gc.easy.redis.impl;


import com.gc.easy.redis.model.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @Auther: gc.x
 * @Date: 2019/5/11 0011 14:10
 * @Description: 可重入锁
 */
public class ReentrantLock extends AbstractLock {

    public ReentrantLock(RedissonClient redissonClient, LockInfo lockInfo) {
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }

    @Override
    protected RLock getLock(String name) {
        return redissonClient.getLock(name);
    }
}
