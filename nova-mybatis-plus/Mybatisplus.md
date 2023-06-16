## nova-mybatis-plus
### 简介:
* [Mybatisplus学习](https://baomidou.com/)
* [学习视频](https://www.bilibili.com/video/BV1Bc411W7Wj?p=18&vd_source=bf69afcaca624e3b61a8f265c720a96b)
* [参考笔记](https://blog.csdn.net/ewertyucf/article/details/130106739)



### 第一章：入门篇
* 引入依赖
~~~xml
<!-- mybatis-plus依赖 里面包含了mybatis-spring-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>${mybatis-plus.version}</version>
</dependency>
~~~

* 修改配置文件
~~~yaml
mybatis-plus:
  #关闭mybatis-plus启动图
  global-config:
    banner: false
  #打印log
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
~~~

### 第二章：基础篇
* 基础的增删改查