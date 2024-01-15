package com.gc.easy.redis.model;

/**
 * @Auther: gc.x
 * @Date: 2019/5/11 0011 13:52
 * @Description: 锁类型
 */
public enum LockType {
    /**
     * 可重入锁
     */
    Reentrant,

    /**
     * 公平锁
     */
    Fair,

    /**
     * 读锁
     */
    Read,

    /**
     * 写锁
     */
    Write;

    LockType() {
    }
}
