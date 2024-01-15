package com.gc.easy.redis;

/**
 * @Auther: gc.x
 * @Date: 2019/5/11 0011 13:47
 * @Description:
 */
public interface ILock {
    /**
     * 获取锁
     *
     * @return
     */
    boolean acquire();

    /**
     * 释放锁
     */
    void release();
}
