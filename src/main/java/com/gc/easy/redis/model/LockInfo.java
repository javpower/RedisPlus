package com.gc.easy.redis.model;

import lombok.Data;

/**
 * @Auther: gc.x
 * @Date: 2019/5/11 0011 13:51
 * @Description: 锁信息
 */
@Data
public class LockInfo {
    private LockType lockType;
    private String name;
    private long waitTime;
    private long leaseTime;

    public LockInfo() {
    }

    public LockInfo(LockType lockType, String name, long waitTime, long leaseTime) {
        this.lockType = lockType;
        this.name = name;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
    }
}
