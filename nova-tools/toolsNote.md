## nova-tools

### 简介:

* (1)练习demo，java8特性，hutool测试类，guava测试类
* (2)sql练习 https://github.com/dongxuyang1985/sql_in_action

##组织结构
```
nova-tools
├── demo --练习demo，线程、springboot、mongoTemplate
├── doc -- 开源advanced面试、jscprout面试
├── java8 -- java8-demo
├── letcode -- 刷题
├── sql -- sql练习
├── txt -- 文本
└── utils -- hutool工具测试类、guava工具测试类、vavr工具测试类
```

## 🛠️包含组件
一个Java基础工具类，对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装，组成各种Util工具类，同时提供以下组件：

| 模块                 | 介绍                                             |
|--------------------|------------------------------------------------|
| hutool-aop         | JDK动态代理封装，提供非IOC下的切面支持                         |
| hutool-bloomFilter | 布隆过滤，提供一些Hash算法的布隆过滤                           |
| hutool-cache       | 简单缓存实现                                         |
| hutool-core        | 核心，包括Bean操作、日期、各种Util等                         |
| hutool-cron        | 定时任务模块，提供类Crontab表达式的定时任务                      |
| hutool-crypto      | 加密解密模块，提供对称、非对称和摘要算法封装                         |
| hutool-db          | JDBC封装后的数据操作，基于ActiveRecord思想                  |
| hutool-dfa         | 基于DFA模型的多关键字查找                                 |
| hutool-extra       | 扩展模块，对第三方封装（模板引擎、邮件、Servlet、二维码、Emoji、FTP、分词等） |
| hutool-http        | 基于HttpUrlConnection的Http客户端封装                  |
| hutool-log         | 自动识别日志实现的日志门面                                  |
| hutool-script      | 脚本执行封装，例如Javascript                            |
| hutool-setting     | 功能更强大的Setting配置文件和Properties封装                 |
| hutool-system      | 系统参数调用封装（JVM信息等）                               |
| hutool-json        | JSON实现                                         |
| hutool-captcha     | 图片验证码实现                                        |
| hutool-poi         | 针对POI中Excel和Word的封装                            |
| hutool-jwt         | JSON Web Token (JWT)封装实现                       |

-------------------------------------------------------------------------------

### Java基础
* [《Java 线程池详解》](https://mp.weixin.qq.com/s/hrPUZQEm6zp76Euj5ScaZw)
* [《可以提高千倍效率的Java代码小技巧》](https://mp.weixin.qq.com/s/LaJ2WeOUiYWzcOI1Na7NVA)
* [《Java性能优化：35个小细节》](https://mp.weixin.qq.com/s/xYTSS9s1N8VXTMrHqaqi9Q)
* [《HashMap？面试？我是谁？我在哪》](https://mp.weixin.qq.com/s/y6hswv2hIm3hAW18SCZYHg)
* [《死磕18个Java8日期处理》](https://mp.weixin.qq.com/s/cuzt_5kiwfWsX2Rx8G6Hug)
* [《Java8 判空新写法！》](https://mp.weixin.qq.com/s/rIhAjO_mTy9WfimS5M7AqA)
* [《20个实例玩转Java8 Stream》](https://mp.weixin.qq.com/s/vFWJrPL3psstPQ4xu3p4gQ)
* [《接口性能优化的11个小技巧》](https://mp.weixin.qq.com/s/XHlnYo-6w2hAdoPZYiFedA)
* [《Java字符串格式示例，很全》](https://mp.weixin.qq.com/s/aVJ87eRZrhqJPPteg2jnzA)
* [《@Transactional原理和常见的坑？》](https://mp.weixin.qq.com/s/dRa99ziZPjUKyFhxFoTaKQ)
* [《你见过哪些目瞪口呆的Java代码技巧？》](https://mp.weixin.qq.com/s/LdX3_bg1UuZx5FygbZUCxw)
* [《你认识强大的Stream和Optional吗？》](https://mp.weixin.qq.com/s/l0mqinIXvXHhZJtU1p_mcg)
* [《阿里的项目到处都是GoogleGuava工具包！》](https://mp.weixin.qq.com/s/OlOHGEfdaFZBqwp9D3DIJw)
* [《Java几种常用JSON库性能比较》](https://mp.weixin.qq.com/s/n0NgvpnXSAddD-eyuuPwFw)
* [《保证接口数据安全的10种方案》](https://mp.weixin.qq.com/s/ftcY--lukUb7LI3OpLDObQ)
* [《生产中遇到cpu过高，我们该如何排查》](https://www.bilibili.com/video/BV1yR4y1r76E/?spm_id_from=333.999.0.0&vd_source=04ff874447812687f3346175b839011e)
* [《秒杀下单-库存超卖》](https://www.bilibili.com/video/BV1Lm4y1P7oY/?spm_id_from=333.999.0.0&vd_source=04ff874447812687f3346175b839011e)
* [多线程事务怎么回滚？说用 @Transactional 可以回去等通知了！](https://mp.weixin.qq.com/s/Vrzelq7JcP_71Mt1OJFS1w)


### 设计模式
* [《Spring框架如何巧妙运用9种设计模式》](https://mp.weixin.qq.com/s/E07FxRIXevQEiFrDfJKMOw)
* [《还在用策略模式解决if-else？ Map+函数式接口方法》](https://mp.weixin.qq.com/s/3Ap4QSPSbRDV6aIRl-UCAw)
* [《多重校验神器责任链模式》](https://mp.weixin.qq.com/s/Yv4PkmiJ1SllelywS7oqYg)

### SQL
* [《52条SQL语句，性能优化》](https://mp.weixin.qq.com/s/R3i6BgHP6yvRLXAxFYsdmA)
* [《批量插入优化》](https://blog.csdn.net/lh155136/article/details/122437056?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522168681407416800182712837%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=168681407416800182712837&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_click~default-1-122437056-null-null.142%5Ev88%5Econtrol_2,239%5Ev2%5Einsert_chatgpt&utm_term=rewriteBatchedStatements%3Dtrue&spm=1018.2226.3001.4187)
* [《SQL优化最强总结 (建议收藏~)》](https://mp.weixin.qq.com/s/SGYJoTYiAilNnNODgGkk3g)
* [《SQL优化万能公式：5大步骤+10个案例》](https://mp.weixin.qq.com/s/_zi661XsJXql68YL8N93Lw)
* [《为什么说Mysql单表行数不要超过2000w? 》](https://mp.weixin.qq.com/s/YEFIe5U-J8Stnh5MvokdLg)
* [《char和varchar有哪些区别？varchar最大长度是多少？》](https://mp.weixin.qq.com/s/Alqk60lcXWrEMFcxj8abAA)
* [《一次SQL查询优化原理分析（900W+ 数据，从17s到300ms》](https://mp.weixin.qq.com/s/vFTxYZmGfML-4PTg4eaQ7g)
* [《8种常被忽视的SQL错误用法 》](https://mp.weixin.qq.com/s/FyzXRZCEUqhWnjP3IJZy9Q)
* [《一次非常有意思的SQL优化经历：从30248.271s到0.001s 》](https://mp.weixin.qq.com/s/tcEoMlz2rdWLsO5q5lO0gw)
* [《Mysql的on duplicate key update和replace into语法有死锁隐患》](https://www.bilibili.com/video/BV16v4y127eG/?spm_id_from=333.999.0.0&vd_source=04ff874447812687f3346175b839011e)
* [《Mysql update 锁住整张表》](https://www.bilibili.com/video/BV1XW4y1u7x2/?spm_id_from=333.999.0.0&vd_source=04ff874447812687f3346175b839011e)
* [《Mysql update 死锁演示》](https://www.bilibili.com/video/BV1SM411B7bC/?spm_id_from=333.999.0.0&vd_source=04ff874447812687f3346175b839011e)
* [《select...for update是锁表还是锁行？》](https://www.bilibili.com/video/BV15f4y137sf/?spm_id_from=333.999.0.0&vd_source=04ff874447812687f3346175b839011e)

### 线程
* [《如何中断一个线程，谈谈具体实现》](https://mp.weixin.qq.com/s/8ZRC4GIteJrXgcAptTAQaw)
* [《一网打尽：异步神器CompletableFuture万字详解》](https://mp.weixin.qq.com/s/EFThWRUWiN4kflfGdwo7xQ)

### 框架
* [《40个SpringBoot常用注解：让生产力爆表》](https://mp.weixin.qq.com/s/cbPuSXycUfreSgso0F9QZg)
* [《SpringBoot16个扩展接口，效率很高！》](https://mp.weixin.qq.com/s/HFwNzOy4buKIuezKx4VUeg)
* [《SpringBoot是如何实现自动配置的？》](https://mp.weixin.qq.com/s/ktsFCUo8_cNe-zBH08_K4g)
* [《如何优雅的写Controller层代码？》](https://mp.weixin.qq.com/s/8tBaZPYj2EPXiFF4qDpt6Q)
* [《掌握这些SpringBoot启动扩展点，已经超过90%的人了》](https://mp.weixin.qq.com/s/4Ggb0QfvhcHEAe3F4gC2YQ)
* [《SpringBoot解决跨域，3种解决方案》](https://mp.weixin.qq.com/s/48Z2sRYb_Ed2BBtNVeDvzg)
* [《从0搭建SpringCloud服务》](https://mp.weixin.qq.com/s/EMae8yQKa9WhiKsYSrT4zw)
* [《Mybatis插入大量数据效率对比》](https://mp.weixin.qq.com/s/npAkhEHCOEkWXoIzJcYrqQ)
* [《MyBatis批量插入别再乱用foreach了》](https://mp.weixin.qq.com/s/rE5hV1muC-HhPPkvsaB5Bg)
* [《关于MyBatis我总结了10种通用的写法》](https://mp.weixin.qq.com/s/tEa873nQXPtRjPgA7_XYvw)
* [《最全的Spring依赖注入方式，你都会了吗？》](https://mp.weixin.qq.com/s/rJ358sJ5YJ6dCNF4Qm6uEw)
* [《SpringAOP看这篇就够了》](https://mp.weixin.qq.com/s/-joW1MSQ1O-XiIaQ0najXA)
* [《日志打印的正确姿势》](https://mp.weixin.qq.com/s/TkM2Be-3dVVlUmwzoJImQA)
* [《SpringBoot+Netty+WebSocket实现消息推送》](https://mp.weixin.qq.com/s/bS1LLtYW7YcH6lpuKUxH2A)
* [《SpringBoot玩一玩代码混淆，防止反编译代码泄露》](https://mp.weixin.qq.com/s/Dz5TFEZg_fs5P-lxcZ2psQ)
* [《SpringBoot+ElasticSearch实现模糊查询，批量CRUD，排序，分页，高亮！》](https://mp.weixin.qq.com/s/qoK_OlUTVnN9fPSWsUF6uw)

### 消息队列
* [《图解Kafka，一目了然！》](https://mp.weixin.qq.com/s/acrsoYKX8xF8DFzrhOnmgg)
* [《RabbitMQ消息重复消费场景及解决方案》](https://mp.weixin.qq.com/s/X8mF1xdX3_7Da5AiR0If0A)

### 缓存
* [《SpringBoot+Redis：模拟10w人的秒杀抢单！》](https://mp.weixin.qq.com/s/o_PUud7DR5ItK83B2f3j5A)
* [《一行代码解决缓存击穿的问题》](https://mp.weixin.qq.com/s/sq8c2hJHiUXJxBg21GAaRg)
* [《有一种数据类型，Redis要存两次，为什么》](https://mp.weixin.qq.com/s/-zhVXT0R2_DwrbVxodgb1w)
* [《SpringBoot+Redis：模拟10w人的，秒杀抢单》](https://mp.weixin.qq.com/s/Hivp90udm8f-3_dn9DLQdg)
* [《Redis如何保证接口的幂等性》](https://mp.weixin.qq.com/s/16nrpU9QEqZ_KUx8WOzJ5w)
* [Lua + Redis + SpringBoot = 王炸！](https://mp.weixin.qq.com/s/JTswEJ8tvcN7XFkxEie2Kw)

### 工具
* [《Java诊断工具Arthas常见命令（超详细实战教程）》](https://mp.weixin.qq.com/s/UnEydFrSyXUb01CF75zcaw)

### 面试
* [《面试必备：30个Java集合面试问题及答案》](https://mp.weixin.qq.com/s/psgJNTZ3B7ZNtiFb67rgDg)
* [《面试官Spring63问，抗住了马上高薪》](https://mp.weixin.qq.com/s/TDCQYAWulmCCCcUn7ok0pQ)
* [《最全的spring面试题和答案》](https://mp.weixin.qq.com/s/N8OkVaRtNlB3xq8KTvo2_g)
