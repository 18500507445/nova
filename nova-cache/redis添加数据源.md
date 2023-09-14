## 如何添加redis多个数据源（Lettuce客户端）

### 1. pom.xml添加依赖
~~~xml
<!-- 第二个数据源需要用的配置包 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
~~~

### 2. yml添加配置，与redis-starter配置相同
~~~yaml
spring:
  redis2:
    database: x
    host: xx.xx.xx.xx
    port: xxxx
    password: xxxxxxxx
    lettuce:
      pool:
        max-active: 500
        max-wait: -1
        max-idle: 20
        min-idle: 20
    timeout: 10000
~~~

### 3. 配置类 RedisConfig
~~~java
    @Value("${spring.redis2.lettuce.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis2.lettuce.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis2.lettuce.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis2.lettuce.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis2.timeout}")
    private int timeout;

    @Bean(name = "secondRedisTemplate")
    public RedisTemplate<String, Object> secondRedisTemplate(@Value("${spring.redis2.database}") int database,
                                                             @Value("${spring.redis2.host}") String hostName,
                                                             @Value("${spring.redis2.port}") int port,
                                                             @Value("${spring.redis2.password}") String password) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory(database, hostName, port, password));
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        return template;
    }

    /**
     * 使用lettuce配置Redis连接信息
     *
     * @param database Redis数据库编号
     * @param hostName 服务器地址
     * @param port     端口
     * @param password 密码
     */
    public RedisConnectionFactory connectionFactory(int database, String hostName, int port, String password) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(hostName);
        configuration.setPort(port);
        if (StrUtil.isNotBlank(password)) {
            configuration.setPassword(password);
        }
        if (database != 0) {
            configuration.setDatabase(database);
        }
        GenericObjectPoolConfig<Object> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWait(Duration.ofMillis(maxWaitMillis));

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .poolConfig(genericObjectPoolConfig)
                .build();

        LettuceConnectionFactory lettuce = new LettuceConnectionFactory(configuration, clientConfig);
        lettuce.afterPropertiesSet();
        return lettuce;
    }
~~~


### 4. 测试，按照beanName注入
~~~java
@Resource
private RedisTemplate<String, Object> secondRedisTemplate;
~~~
