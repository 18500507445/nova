## nova-monitor

### 简介:
* [SBA监控后台](https://github.com/codecentric/spring-boot-admin)

#### 1.1 服务端引入依赖
~~~xml
 <dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!--SBA服务端后台 管理和监控Spring Boot应用程序-->
    <dependency>
        <groupId>de.codecentric</groupId>
        <artifactId>spring-boot-admin-starter-server</artifactId>
        <version>${sba.version}</version>
    </dependency>
</dependencies>
~~~

#### 1.2 [服务端访问地址](http://localhost:8888/applications)

#### 1.3 启动类开启注解
~~~java
@EnableAdminServer
@SpringBootApplication
~~~

#### 2.1 客户端引入依赖
~~~xml
 <dependencies>
    <!-- SBA监控客户端 -->
    <dependency>
        <groupId>de.codecentric</groupId>
        <artifactId>spring-boot-admin-starter-client</artifactId>
        <version>${sba.version}</version>
    </dependency>

    <!-- 更多的监控需要的依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
~~~

#### 2.2 [引入actuator依赖，查看暴露的端点](http://localhost:8080/actuator)

#### 2.3 客户端添加配置
~~~yaml
#SBA监控客户端配置
management:
  endpoints:
    web:
      exposure:
        # 暴露所有端点
        include: "*"
    # 是否启用actuator，默认true
    enabled-by-default: true
  endpoint:
    health:
      # 健康检查显示详细信息（db，redis，diskSpace，ping...），默认never，只显示一个up
      show-details: always

spring:
  #SBA服务端地址
  boot:
    admin:
      client:
        url: http://localhost:8888
~~~

### 收集的知识
* [《百度》](https://www.baidu.com)

