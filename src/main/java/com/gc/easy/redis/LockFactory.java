package com.gc.easy.redis;

import com.gc.easy.redis.impl.FairLock;
import com.gc.easy.redis.impl.ReadLock;
import com.gc.easy.redis.impl.ReentrantLock;
import com.gc.easy.redis.impl.WriteLock;
import com.gc.easy.redis.model.LockInfo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class LockFactory {
    @Autowired
    private RedissonClient redissonClient;

    @SuppressWarnings("all")
    public ILock getLock(LockInfo lockInfo) {
        switch (lockInfo.getLockType()) {
            case Reentrant:
                return new ReentrantLock(redissonClient, lockInfo);
            case Fair:
                return new FairLock(redissonClient, lockInfo);
            case Read:
                return new ReadLock(redissonClient, lockInfo);
            case Write:
                return new WriteLock(redissonClient, lockInfo);
            default:
                return new ReentrantLock(redissonClient, lockInfo);
        }
    }

}
