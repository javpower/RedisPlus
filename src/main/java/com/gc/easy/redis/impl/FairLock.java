package com.gc.easy.redis.impl;

import com.gc.easy.redis.model.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @Auther: gc.x
 * @Date: 2019/5/11 0011 14:08
 * @Description: 公平锁
 */
public class FairLock extends AbstractLock {

    public FairLock(RedissonClient redissonClient, LockInfo lockInfo) {
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }

    @Override
    protected RLock getLock(String name) {
        return redissonClient.getFairLock(name);
    }
}
