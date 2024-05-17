
# 📚 RedisPlus: 高级Redisson客户端封装

`RedisPlus` 是一个基于 `Redisson` 的高级Java工具库，它通过注解的方式简化了分布式锁、消息队列、发布订阅等Redis特性的使用。该库旨在提供声明式、高度可配置的Redis操作，以适应各种复杂的业务场景。

## 🌈 特性概览

- **分布式锁**：支持可重入锁、公平锁、读写锁等多种锁类型。
- **消息队列**：自动注册消息队列监听器，简化消息消费。
- **发布订阅**：实现发布订阅模式，异步处理消息发布和订阅。
- **配置丰富**：提供丰富的配置选项，包括连接池、超时设置、哨兵支持等。
- **重试机制**：支持失败操作的自动重试，提高系统稳定性。
- **工具类**：提供 `RedisUtil` 工具类，简化CRUD操作。

## 🛠️ 快速开始

### 添加依赖

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.github.javpower</groupId>
    <artifactId>easy-redis-spring-boot-starter</artifactId>
    <version>3.9.1.1</version>
</dependency>
```

### 配置Redisson

在 `application.yml` 中配置 `Redisson` 和 `Spring` 的Redis连接属性：

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your-password

redisson:
  timeout: 3000            # 超时时间（毫秒）
  address: "redis://127.0.0.1:6379" # Redis地址
  password: "your-password" # Redis密码
  database: 0              # 选择的数据库
  connectionPoolSize: 64  # 连接池大小
  connectionMinimumIdleSize: 10 # 连接池中最小空闲连接数
  slaveConnectionPoolSize: 250 # 从服务器连接池大小
  masterConnectionPoolSize: 250 # 主服务器连接池大小
  sentinelAddresses: ["redis://127.0.0.1:26379"] # 哨兵模式下的地址数组
  masterName: "myMaster" # 哨兵模式下的主节点名称
  scanInterval: 2000      # 扫描间隔（毫秒）
```

### 使用分布式锁

在需要加锁的方法上使用 `@Lock` 注解：

```java
@Lock(name = "myLock", lockType = LockType.Reentrant, waitTime = 10, leaseTime = 300)
public void criticalSection() {
    // 临界区代码
}
```

### 消息队列与发布订阅

通过 `QueueRegistrar` 和 `SubscriberRegistrar` 自动注册消息队列和发布订阅处理器。

### 异常重试

使用 `@Retry` 注解来处理方法调用的重试：

```java
@Retry(retryNumber = 3)
public void operationWithRetry() {
    // 可能会失败的操作
}
```

### 工具类使用

`RedisUtil` 提供了一系列工具方法：

```java
@Autowired
private RedisUtil redisUtil;

public void performOperation() {
    Long incrementResult = redisUtil.increment("myCounter");
    // 使用RedisUtil进行其他操作...
}
```

## 🔍 详细文档

### 消息队列监听

创建一个 `@Component` 类，并在需要监听消息的方法上使用 `@Queue` 注解：

```java
@Component
public class QueueConsumer {

    @Queue(topic = "myQueue")
    public void consumeMessage(String message) {
        // 处理消息
    }
}
```

### 发布订阅模型

实现一个发布订阅监听器，并使用 `@Subscribe` 注解：

```java
@Component
public class MessageSubscriber {

    @Subscribe(topic = "myTopic")
    public void onMessageReceived(String message) {
        // 处理消息
    }
}
```

### 异常重试机制

通过 `@Retry` 注解实现方法调用的自动重试：

```java
public class ServiceWithRetry {

    @Retry(retryNumber = 5, waitTime = 1000)
    public void operationWithRetry() {
        // 可能会失败的操作
    }
}
```

### 工具类操作

`RedisUtil` 提供了一系列Redis操作的简化方法：

```java
public class SomeService {

    @Autowired
    private RedisUtil redisUtil;

    public void someMethod() {
        boolean isKeyExist = redisUtil.hasKey("myKey");
        // 其他Redis操作...
    }
}
```

## 📋 注意事项

- 本项目尚未声明开源许可证，使用时请遵守相关法律法规。
- 请确保您的Redis服务器配置正确，并且稳定运行。
- 在生产环境中使用前，请进行充分的测试以确保系统稳定性。

## 🤝 贡献指南

我们非常欢迎社区贡献，以下是参与 `easy-redis` 的步骤：

1. **克隆仓库**：Fork并克隆 `easy-redis` 仓库。
2. **开发新特性**：创建一个新分支并实现您的特性或修复bug。
3. **提交更改**：提交您的更改，并确保包含测试以验证您的代码。
4. **创建Pull Request**：向 `easy-redis` 的主仓库发起Pull Request。

## 📜 许可证

项目许可证尚未明确，使用前请查看项目页面的 `LICENSE` 文件。

## 🙏 致谢

感谢 `Redisson` 团队提供的出色客户端库，以及所有为 `easy-redis` 做出贡献的开发者。
