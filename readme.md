## 欢迎来到nova
* 简介:日常积累代码、读书和学习笔记

### 结构介绍
1.nova-book
* [书籍和学习笔记](nova-book/bookNote.md)
* [数据结构与算法](nova-book/src/main/java/com/nova/book/algorithm/algorithm.md)
* [设计模式](nova-book/src/main/java/com/nova/book/design/design.md)
* [Java并发编程](nova-book/src/main/java/com/nova/book/juc/juc.md)
* [HashMap解读](nova-book/src/main/java/com/nova/book/hashmap/hashmap解读.md)
* [xxl-job源码笔记](https://www.processon.com/preview/6433f533b433fa00159576a8)

2.nova-cache
* nova-cache-redis：Memcached、caffeine、redis（lettuce客户端）
* nova-cache-lock：[redisson分布式锁](nova-cache/lockNote.md)
* nova-cache-limit：[接口限流、请求，响应拦截器](nova-cache/limitNote.md)
* ![](./img/redis客户端对比.jpg)
* redis与jedis客户端对比，不推荐使用jedis。1.线程不安全需要连接池 2.相比性能较差

3.nova-common
* 框架核心组件、通用工具包（包括常量类，工具类，枚举类，异常类）

4.nova-database
* 数据库组件（动态数据源）

5.nova-excel
* [excel工具类](nova-excel/excelNote.md)

6.nova-log
* [异步log](nova-log/logNote.md)
* logback与log4j2性能对比
* log中拦截器的应用

7.nova-login
* [sa-token登录](nova-login/loginNote.md)

8.nova-monitor
* [SBA监控后台](nova-monitor/sbaNote.md)

9.nova-mq
* active，kafka，rabbit，rocket（待定）

10.nova-msg
* mail-发送邮件
* sms-（1）多短信通道：阿里云、腾讯云、七牛云、云片   （2）Sms4j现成框架，开箱即用

11.nova-orm
* [mybatis学习](nova-orm/nova-orm-mybatis/mybatisNote.md)
* [mybatisplus整合pageHelper学习](nova-orm/nova-orm-mybatisplus/mybatisplus.md)
* [mybatisflex学习](nova-orm/nova-orm-mybatisflex/mybatisflex.md)

12.nova-rpc
* [手写rpc框架](nova-rpc/nova-rpc-socket/rpcNote.md)
* [websocket学习]

13.nova-search
* elasticsearch、solr

14.nova-starter（后续mq....）
* 自定义starter：redis、mongo、xxlJob
* <optional>true</optional>：防止依赖传递，导致引入后和项目的包冲突

15.nova-tools
* [练习demo，java8，hutool、guava测试类，微信公众号文章](nova-tools/toolsNote.md)
* [互联网Java工程师进阶知识完全扫盲-advanced](summary.md)
* Sql练习
* 使用starter详情见测试类
* 配置文件加解密测试类（JasyptTest）

~~~yml
jasypt:
  encryptor:
    # 加密算法
    algorithm: PBEWITHHMACSHA512ANDAES_256
    # 加密使用的盐
    password: jaspyt_password
~~~
