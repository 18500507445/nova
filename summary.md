- 高并发架构

  - [消息队列](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/mq-interview.md)

    - [为什么使用消息队列？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/why-mq.md)
    - [如何保证消息队列的高可用？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/how-to-ensure-high-availability-of-message-queues.md)
    - [如何保证消息不被重复消费？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/how-to-ensure-that-messages-are-not-repeatedly-consumed.md)
    - [如何保证消息的可靠性传输？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/how-to-ensure-the-reliable-transmission-of-messages.md)
    - [如何保证消息的顺序性？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/how-to-ensure-the-order-of-messages.md)
    - [如何解决消息队列的延时以及过期失效问题？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/mq-time-delay-and-expired-failure.md)
    - [如何设计一个消息队列？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/mq-design.md)

  - [搜索引擎](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/es-introduction.md)

    - [ES 的分布式架构原理是什么？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/es-architecture.md)
    - [ES 写入数据的工作原理是什么？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/es-write-query-search.md)
    - [ES 在数十亿级别数量下如何提高查询效率？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/es-optimizing-query-performance.md)
    - [ES 生产集群的部署架构是什么？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/es-production-cluster.md)

  - 缓存

    - [在项目中缓存是如何使用的？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/why-cache.md)
    - [Redis 和 Memcached 有什么区别？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-single-thread-model.md)
    - [Redis 都有哪些数据类型以及适用场景？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-data-types.md)
    - [Redis 的过期策略都有哪些？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-expiration-policies-and-lru.md)
    - [如何保证 Redis 高并发、高可用？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/how-to-ensure-high-concurrency-and-high-availability-of-redis.md)
    - [Redis 主从架构是怎样的？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-master-slave.md)
    - [Redis 的持久化有哪几种方式？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-persistence.md)
    - [Redis 如何基于哨兵集群实现高可用？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-sentinel.md)
    - [Redis 集群模式的工作原理能说一下么？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-cluster.md)
    - [Redis 的雪崩、穿透和击穿，如何应对？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-caching-avalanche-and-caching-penetration.md)
    - [如何保证缓存与数据库双写一致性？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-consistence.md)
    - [如何解决 Redis 的并发竞争问题？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-cas.md)
    - [生产环境中的 Redis 是怎么部署的？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/redis-production-environment.md)

  - 分库分表

    - [为什么要分库分表？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/database-shard.md)
    - [分库分表如何平滑过渡？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/database-shard-method.md)
    - [设计一个动态扩容缩容的分库分表方案？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/database-shard-dynamic-expand.md)
    - [分库分表之后，id 主键如何处理？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/database-shard-global-id-generate.md)

  - 读写分离

    - [如何实现 MySQL 的读写分离？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/mysql-read-write-separation.md)

  - 高并发系统
    - [如何设计一个高并发系统？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/high-concurrency-design.md)

* 分布式系统

  - [面试连环炮](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/distributed-system-interview.md)
  - 系统拆分

    - [为什么要进行系统拆分？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/why-dubbo.md)

  - 分布式服务框架

    - [说一下 Dubbo 的工作原理？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/dubbo-operating-principle.md)
    - [Dubbo 支持哪些序列化协议？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/dubbo-serialization-protocol.md)
    - [Dubbo 负载均衡策略和集群容错策略？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/dubbo-load-balancing.md)
    - [Dubbo 的 SPI 思想是什么？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/dubbo-spi.md)
    - [如何基于 Dubbo 进行服务治理？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/dubbo-service-management.md)
    - [分布式服务接口的幂等性如何设计？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/distributed-system-idempotency.md)
    - [分布式服务接口请求的顺序性如何保证？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/distributed-system-request-sequence.md)
    - [如何自己设计一个类似 Dubbo 的 RPC 框架？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/dubbo-rpc-design.md)
    - [CAP 定理的 P 是什么？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/distributed-system-cap.md)

  - 分布式锁

    - [Zookeeper 都有哪些应用场景？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/zookeeper-application-scenarios.md)
    - [分布式锁如何设计？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/distributed-lock-redis-vs-zookeeper.md)

  - 分布式事务

    - [分布式事务了解吗？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/distributed-transaction.md)

  - 分布式会话
    - [集群分布式 Session 如何实现？](nova-tools/src/main/java/com/nova/tools/doc/advanced/distributed-system/distributed-session.md)

* 高可用架构

  - 基于 Hystrix 实现高可用

    - [Hystrix 介绍](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/hystrix-introduction.md)
    - [电商网站详情页系统架构](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/e-commerce-website-detail-page-architecture.md)
    - [Hystrix 线程池技术实现资源隔离](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/hystrix-thread-pool-isolation.md)
    - [Hystrix 信号量机制实现资源隔离](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/hystrix-semphore-isolation.md)
    - [Hystrix 隔离策略细粒度控制](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/hystrix-execution-isolation.md)
    - [深入 Hystrix 执行时内部原理](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/hystrix-process.md)
    - [基于 request cache 请求缓存技术优化批量商品数据查询接口](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/hystrix-request-cache.md)
    - [基于本地缓存的 fallback 降级机制](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/hystrix-fallback.md)
    - [深入 Hystrix 断路器执行原理](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/hystrix-circuit-breaker.md)
    - [深入 Hystrix 线程池隔离与接口限流](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/hystrix-thread-pool-current-limiting.md)
    - [基于 timeout 机制为服务接口调用超时提供安全保护](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/hystrix-timeout.md)

  - 高可用系统

    - 如何设计一个高可用系统？

  - 限流

    - [如何限流？说一下具体的实现？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-concurrency/how-to-limit-current.md)

  - 熔断

    - 如何进行熔断？
    - 熔断框架都有哪些？具体实现原理知道吗？
    - [熔断框架，选用 Sentinel 还是 Hystrix？](nova-tools/src/main/java/com/nova/tools/doc/advanced/high-availability/sentinel-vs-hystrix.md)

  - 降级
    - 如何进行降级？

* 微服务架构

  - 微服务的一些概念

    - [关于微服务架构的描述](nova-tools/src/main/java/com/nova/tools/doc/advanced/micro-services/microservices-introduction.md)
    - [从单体式架构迁移到微服务架构](nova-tools/src/main/java/com/nova/tools/doc/advanced/micro-services/migrating-from-a-monolithic-architecture-to-a-microservices-architecture.md)
    - [微服务的事件驱动数据管理](nova-tools/src/main/java/com/nova/tools/doc/advanced/micro-services/event-driven-data-management-for-microservices.md)
    - [选择微服务部署策略](nova-tools/src/main/java/com/nova/tools/doc/advanced/micro-services/choose-microservice-deployment-strategy.md)

  - Spring Cloud 微服务架构
    - [什么是微服务？微服务之间是如何独立通讯的？](nova-tools/src/main/java/com/nova/tools/doc/advanced/micro-services/what's-microservice-how-to-communicate.md)
    - Spring Cloud 和 Dubbo 有哪些区别？
    - Spring Boot 和 Spring Cloud，谈谈你对它们的理解？
    - 什么是服务熔断？什么是服务降级？
    - 微服务的优缺点分别是什么？说一下你在项目开发中碰到的坑？
    - [你所知道的微服务技术栈都有哪些？](nova-tools/src/main/java/com/nova/tools/doc/advanced/micro-services/micro-services-technology-stack.md)
    - [微服务治理策略](nova-tools/src/main/java/com/nova/tools/doc/advanced/micro-services/micro-service-governance.md)
    - Eureka 和 Zookeeper 都可以提供服务注册与发现的功能，它们有什么区别？
    - [谈谈服务发现组件 Eureka 的主要调用过程？](nova-tools/src/main/java/com/nova/tools/doc/advanced/micro-services/how-eureka-enable-service-discovery-and-service-registration.md)

* 海量数据处理
  - 10 道经典的海量数据处理面试题
    - [如何从大量的 URL 中找出相同的 URL？](nova-tools/src/main/java/com/nova/tools/doc/advanced/big-data/find-common-urls.md)
    - [如何从大量数据中找出高频词？](nova-tools/src/main/java/com/nova/tools/doc/advanced/big-data/find-top-100-words.md)
    - [如何找出某一天访问百度网站最多的 IP？](nova-tools/src/main/java/com/nova/tools/doc/advanced/big-data/find-top-1-ip.md)
    - [如何在大量的数据中找出不重复的整数？](nova-tools/src/main/java/com/nova/tools/doc/advanced/big-data/find-no-repeat-number.md)
    - [如何在大量的数据中判断一个数是否存在？](nova-tools/src/main/java/com/nova/tools/doc/advanced/big-data/find-a-number-if-exists.md)
    - [如何查询最热门的查询串？](nova-tools/src/main/java/com/nova/tools/doc/advanced/big-data/find-hotest-query-string.md)
    - [如何统计不同电话号码的个数？](nova-tools/src/main/java/com/nova/tools/doc/advanced/big-data/count-different-phone-numbers.md)
    - [如何从 5 亿个数中找出中位数？](nova-tools/src/main/java/com/nova/tools/doc/advanced/big-data/find-mid-value-in-500-millions.md)
    - [如何按照 query 的频度排序？](nova-tools/src/main/java/com/nova/tools/doc/advanced/big-data/sort-the-query-strings-by-counts.md)
    - [如何找出排名前 500 的数？](nova-tools/src/main/java/com/nova/tools/doc/advanced/big-data/find-rank-top-500-numbers.md)
