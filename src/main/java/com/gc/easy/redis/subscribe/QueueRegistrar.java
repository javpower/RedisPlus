package com.gc.easy.redis.subscribe;

import com.gc.easy.redis.annotation.Queue;
import com.gc.easy.redis.annotation.Subscribe;
import io.micrometer.core.instrument.util.StringUtils;
import org.redisson.api.RBlockingQueue;
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
 * @Quenue 消息队列模式
 *
 * @Component
 * public class TestSubscriber {
 *     @Quenue(topic = "myTopic")
 *     public void handleMessage(String message) {
 *         // 处理接收到的消息
 *         System.out.println("Received message: " + message);
 *     }
 * }
 */
@Component
public class QueueRegistrar implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(redissonClient==null) return;
        ApplicationContext context = event.getApplicationContext();
        // 扫描所有bean
        String[] beanNames = context.getBeanNamesForAnnotation(Component.class);
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                Queue annotation = method.getAnnotation(Queue.class);
                if (annotation != null) {
                    String topic = annotation.topic();
                    RBlockingQueue<String> queue = redissonClient.getBlockingQueue(topic);
                    new Thread(() -> {
                        try {
                            while (true) {
                                String message = queue.take();
                                try {
                                    // 处理消息的逻辑
                                    method.invoke(bean, message);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }).start();
                }
            }
        }
    }
}
