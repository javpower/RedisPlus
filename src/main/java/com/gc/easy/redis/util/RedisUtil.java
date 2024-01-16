package com.gc.easy.redis.util;

import org.redisson.api.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private final RedissonClient redissonClient;

    public RedisUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 发布订阅的消息
     * @param topic
     * @param message
     */
    public void pushSubscriberMessage(String topic,String message) {
        RTopic rtopic = redissonClient.getTopic(topic);
        rtopic.publish(message);
    }

    /**
     * 发布消息队列
     * @param topic
     * @param message
     */
    public void pushQueueMessage(String topic,String message) {
        RBlockingQueue<String> queue = redissonClient.getBlockingQueue(topic);
        queue.offer(message);
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
