package com.gc.easy.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: gc.x
 * @Date: 2020/8/7 9:45
 * @Description:
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Retry {
    /**
     * 重试次数
     *
     * @return
     */
    int retryNumber() default 0;
}
