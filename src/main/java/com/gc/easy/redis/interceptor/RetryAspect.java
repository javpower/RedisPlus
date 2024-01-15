package com.gc.easy.redis.interceptor;

import com.gc.easy.redis.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


/**
 * @Auther: gc.x
 * @Date: 2020/8/7 9:50
 * @Description:
 */

@Component
@Aspect
@Slf4j
public class RetryAspect {

    @Around(value = "@annotation(retry)")
    public Object process(ProceedingJoinPoint invocation, Retry retry) throws Throwable {
        long number = retry.retryNumber();
        while (--number >= 0) {
            try {
                return invocation.proceed();
            } catch (Throwable t) {
                log.error("[方法:{}]执行异常:{},正在进行第{}次重试", invocation.getTarget(), t.getMessage(), number);
            }
        }
        return invocation.proceed();
    }
}
