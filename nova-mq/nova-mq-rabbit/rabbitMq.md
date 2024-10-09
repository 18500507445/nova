RabbitMQ ：性能很强，吞吐量很高，支持多种协议，集群化，消息的可靠执行特性等优势，很适合企业的开发。

[安装Rabbit](https://wanghuichen.blog.csdn.net/article/details/124797746?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EOPENSEARCH%7ERate-5-124797746-blog-117995202.pc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EOPENSEARCH%7ERate-5-124797746-blog-117995202.pc_relevant_multi_platform_whitelistv3&utm_relevant_index=10)

进入了之后会显示当前的消息队列情况，包括版本号、Erlang版本等，这里需要介绍一下RabbitMQ的设计架构，这样我们就知道各个模块管理的是什么内容了：
![](https://img-blog.csdnimg.cn/304b9d25cb744fefa4c097ef19be5b7a.png#pic_center)

* Server：又称Broker ,接受客户端的连接，实现AMQP实体服务。 安装rabbitmq-server
* Connection：连接，应用程序与Broker的网络连接 TCP/IP/ 三次握手和四次挥手
* Channel：网络信道，几乎所有的操作都在Channel中进行，Channel是进行消息读写的通道，客户端可以建立对各Channel，每个Channel代表一个会话任务。
* Message :消息：服务与应用程序之间传送的数据，由Properties和body组成，Properties可是对消息进行修饰，比如消息的优先级，延迟等高级特性，Body则就是消息体的内容。
* Virtual Host 虚拟地址，用于进行逻辑隔离，最上层的消息路由，一个虚拟主机理由可以有若干个Exchange和Queue，同一个虚拟主机里面不能有相同名字的Exchange
* Exchange：交换机，接受消息，根据路由键发送消息到绑定的队列。(==不具备消息存储的能力==)
* Bindings：Exchange和Queue之间的虚拟连接，binding中可以保护多个routing key.
* Routing key：是一个路由规则，虚拟机可以用它来确定如何路由一个特定消息。
* Queue：队列：也成为Message Queue,消息队列，保存消息并将它们转发给消费者。


## 1. 使用消息队列
项目中代码演示了3个交换机模式，简单分发和公平、轮询分发(这两个绑定的默认的直连交换机)

## 2. 批量发送
```java
@Configuration
public class RabbitConfiguration {
    @Resource
    private ConnectionFactory connectionFactory;

    /**
     * 注入一个批量 template
     * Spring-AMQP 通过 BatchingRabbitTemplate 提供批量发送消息的功能。如下是三个条件，满足任一即会批量发送：
     * 
     * 【数量】batchSize ：超过收集的消息数量的最大条数。
     * 【空间】bufferLimit ：超过收集的消息占用的最大内存。
     * 【时间】timeout ：超过收集的时间的最大等待时长，单位：毫秒。
     *  不过要注意，这里的超时开始计时的时间，是以最后一次发送时间为起点。也就说，每调用一次发送消息，都以当前时刻开始计时，重新到达 timeout 毫秒才算超时。
     */
    @Bean
    public BatchingRabbitTemplate batchRabbitTemplate() {
        // 创建 BatchingStrategy 对象，代表批量策略
        // 超过收集的消息数量的最大条数。
        int batchSize = 10;
        // 每次批量发送消息的最大内存 b
        int bufferLimit = 1024 * 1024;
        // 超过收集的时间的最大等待时长，单位：毫秒
        int timeout = 10 * 1000;
        BatchingStrategy batchingStrategy = new SimpleBatchingStrategy(batchSize, bufferLimit, timeout);
        // 创建 TaskScheduler 对象，用于实现超时发送的定时器
        TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
        // 创建 BatchingRabbitTemplate 对象
        BatchingRabbitTemplate batchTemplate = new BatchingRabbitTemplate(batchingStrategy, taskScheduler);
        batchTemplate.setConnectionFactory(connectionFactory);
        return batchTemplate;
    }
}
```


## 3. 批量消费
```java
@Configuration
public class ConsumerConfiguration {
    @Resource
    private ConnectionFactory connectionFactory;
    @Resource
    private SimpleRabbitListenerContainerFactoryConfigurer configurer;

    /**
     * 配置一个批量消费的 SimpleRabbitListenerContainerFactory
     */
    @Bean(name = "consumerBatchContainerFactory")
    public SimpleRabbitListenerContainerFactory consumerBatchContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        // 这里是重点 配置消费者的监听器是批量消费消息的类型
        factory.setBatchListener(true);

        // 一批十个
        factory.setBatchSize(10);
        // 等待时间 毫秒 , 这里其实是单个消息的等待时间 指的是单个消息的等待时间
        // 也就是说极端情况下，你会等待 BatchSize * ReceiveTimeout 的时间才会收到消息
        factory.setReceiveTimeout(10 * 1000L);
        factory.setConsumerBatchEnabled(true);

        return factory;
    }
}
```

### 3.1 批量消费，消费者配置
```java
@Component
@RabbitListener(queues = "queueName", containerFactory = "consumerBatchContainerFactory")
public class BatchConsumer {
    @RabbitHandler
    public void process(List<String> messages) {
        System.out.println("批量消费消息：" + messages);
    }
}
```


## 4. 如何保证顺序消费
一般我们讨论如何保证消息的顺序性，会从下面三个方面考虑  
1. 发送消息的顺序  
2. 队列中消息的顺序  
3. 消费消息的顺序  

### 4.1 发送消息的顺序
消息发送端的顺序，大部分业务不做要求，谁先发消息无所谓，如果遇到业务一定要发送消息也确保顺序，那意味着，只能全局加锁一个个的操作，一个个的发消息，不能并发发送消息。

### 4.2 队列中消息的顺序
RabbitMQ 中，消息最终会保存在队列中，在同一个队列中，消息是顺序的，先进先出原则，这个由 RabbitMQ 保证，通常也不需要开发关心。
> **不同队列** 中的消息顺序，是没有保证的，例如：进地铁站的时候，排了三个队伍，不同队伍之间的，不能确保谁先进站。

### 4.3 消费消息的顺序
我们说如何保证消息顺序性，通常说的就是消费者消费消息的顺序，在多个消费者消费同一个消息队列的场景，通常是无法保证消息顺序的，
虽然消息队列的消息是顺序的，但是多个消费者并发消费消息，获取的消息的速度、执行业务逻辑的速度快慢、执行异常等等原因都会导致消息顺序不一致。
例如：消息A、B、C按顺序进入队列，消费者A1拿到消息A、消费者B1拿到消息B, 结果消费者B执行速度快，就跑完了，又或者消费者A1挂了，都会导致消息顺序不一致。

解决消费顺序的问题，通常就是一个队列只有一个消费者,这样就可以一个个消息按顺序处理，缺点就是并发能力下降了，无法并发消费消息，这是个取舍问题。

> 如果业务又要顺序消费，又要增加并发，通常思路就是开启多个队列，业务根据规则将消息分发到不同的队列，通过增加队列的数量来提高并发度，例如：电商订单场景，只需要保证同一个用户的订单消息的顺序性就行，不同用户之间没有关系，所以只要让同一个用户的订单消息进入同一个队列就行，其他用户的订单消息，可以进入不同的队列。

解决消费顺序的问题，通常就是一个队列只有一个消费者，这样就可以一个个消息按顺序处理，缺点就是并发能力下降了，无法并发消费消息，这是个取舍问题。

首先我们必须保证只有一个消费者  
那么问题就来了，我们的项目一般是多副本的，如何保证只有一个副本在消费呢  
这时就会用到消费者 **单活模式** `x-single-active-consumer`  

```java
@Configuration
public class RabbitConfig {
    private Queue creatQueue(String name) {
        // 创建一个 单活模式 队列
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-single-active-consumer", true);
        return new Queue(name, true, false, false, args);
    }
}
```

## 5. MQ中的事务，注入事务管理器
需要注入一个事务管理器
```java
@Configuration
public class RabbitConfiguration {
    @Resource
    private ConnectionFactory connectionFactory;
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 注入一个事务管理器 RabbitTransactionManager
     */
    @Bean
    public RabbitTransactionManager rabbitTransactionManager() {
        // 设置 RabbitTemplate 支持事务
        rabbitTemplate.setChannelTransacted(true);
        // 创建 RabbitTransactionManager 对象
        return new RabbitTransactionManager(connectionFactory);
    }
}
```

## 5.1 使用
配合 `@Transactional` 声明式事务
```java
@Component
@Slf4j
public class Producer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 在发送消息方法上，我们添加了 @Transactional 注解，声明事务。
     * 因为我们创建了 RabbitTransactionManager 事务管理器，所以这里会创建 RabbitMQ 事务
     * <p>
     * 当然也可以使用编程式事务
     * channel.txSelect();
     * channel.basicPublish();
     * channel.txCommit();
     * channel.txRollback(); // 回滚事务
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void send(String id, String routingKey) throws InterruptedException {
        // 同步发送消息
        rabbitTemplate.convertAndSend(exchangeName, routingKey, id);
        /**
         * 睡上 10s 方便看效果
         * 如果同步发送消息成功后，Consumer 立即消费到该消息，说明未生效
         * 如果 Consumer 是 10 秒之后，才消费到该消息，说明已生效
         */
        TimeUnit.SECONDS.sleep(10);
    }
}
```


## rabbitMQ常见问题
1. rabbitmq的长时间未消费报错。[rabbitmq的consumer_timeout修改](https://www.cnblogs.com/long757747969/p/16936604.html)