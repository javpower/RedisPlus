package com.gc.easy.redis.redisson;

import io.micrometer.core.instrument.util.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: gc.x
 * @Date: 2020/4/18 8:27
 * @Description:
 */

@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfig {

    @Autowired
    private RedissonProperties redssionProperties;

    /**
     * 单体的
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "redisson.address")
    public RedissonClient redissonSingle() {
        Config config = new Config();
        String address = redssionProperties.getAddress();
        address = address.startsWith("redis://") ? address : "redis://" + address;
        SingleServerConfig serverConfig = config.useSingleServer().setAddress(address)
                .setTimeout(redssionProperties.getTimeout())
                .setDatabase(redssionProperties.getDatabase())
                .setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());
        if (StringUtils.isNotEmpty(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 集群的
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "redisson.masterAddresses")
    public RedissonClient redissonSentinel() {
        Config config = new Config();
        ClusterServersConfig serverConfig = config.useClusterServers().addNodeAddress(redssionProperties.getSentinelAddresses())
                .setTimeout(redssionProperties.getTimeout())
                //设置集群扫描时间
                .setScanInterval(redssionProperties.getScanInterval())
                //主节点线程池数量
                .setMasterConnectionPoolSize(redssionProperties.getMasterConnectionPoolSize())
                //从节点线程池数量
                .setSlaveConnectionPoolSize(redssionProperties.getSlaveConnectionPoolSize());

        if (StringUtils.isNotEmpty(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }
        return Redisson.create(config);
    }
}
