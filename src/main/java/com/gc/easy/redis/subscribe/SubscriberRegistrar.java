package com.gc.easy.redis.subscribe;

import com.gc.easy.redis.annotation.Subscribe;
import io.micrometer.core.instrument.util.StringUtils;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Subscribe自动订阅
 *
 * @Component
 * public class TestSubscriber {
 *     @AutoSubscribe(topic = "myTopic")
 *     public void handleMessage(String message) {
 *         // 处理接收到的消息
 *         System.out.println("Received message: " + message);
 *     }
 * }
 */
@Component
public class SubscriberRegistrar implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(redissonClient==null) return;
        ApplicationContext context = event.getApplicationContext();
        // 扫描所有bean
        String[] beanNames = context.getBeanNamesForAnnotation(Subscribe.class);
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                Subscribe annotation = method.getAnnotation(Subscribe.class);
                if (annotation != null) {
                    String topic = annotation.topic();
                    // 注册订阅者
                    RTopic rTopic = redissonClient.getTopic(topic);
                    rTopic.addListener(String.class, (charSequence, msgStr) -> {
                        try {
                            if (StringUtils.isNotEmpty(msgStr)) {
                                method.invoke(bean, msgStr);
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }
}
