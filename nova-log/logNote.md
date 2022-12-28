## nova-log
### 简介:
* 异步日志
* springboot默认 `spring-boot-starter-logging`依赖，配置读取的logback-spring.xml
* 如何开启异步日志呢，切换到Log4j2(异步性能好)、引入disruptor依赖，排除springboot默认的logback依赖，
  配置读取的log4j2-spring.xml，启动类添加`System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");`
~~~xml
<dependencies>
    
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <exclusions>
        <!--排除springboot默认的logback依赖 -->
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>

<dependency>
    <groupId>com.lmax</groupId>
    <artifactId>disruptor</artifactId>
    <version>${disruptor.version}</version>
</dependency>

</dependencies>
~~~

### 收集的知识
* [《百度》](https://www.baidu.com)

