package com.gc.easy.redis.util;

import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private final RedissonClient redissonClient;

    public RedisUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /*
        发布订阅的消息
     */
    public void pushSubscriberMessage(String topic,String message) {
        RTopic rtopic = redissonClient.getTopic(topic);
        rtopic.publish(message);
    }
    public <T> T get(String key, Class<T> clazz) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }
    public boolean set(String key, Object value) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.trySet(value);
    }
    public boolean set(String key, Object value, int expireTime) {
        RMap<String, Object> map = redissonClient.getMap(key);
        map.expire(expireTime, TimeUnit.SECONDS);
        return map.fastPut("", value);
    }
    public boolean delete(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.delete();
    }
    public boolean hasKey(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.isExists();
    }
}
