## nova-database
### 简介:
* 方式一：自定义动态数据源（注解直接作用于Service或者方法上）
~~~java
@DataSource(value = DataSourceType.MASTER)
~~~

* 方式二：[《动态数据源依赖》](https://mvnrepository.com/artifact/com.baomidou/dynamic-datasource-spring-boot-starter)
~~~xml
<dependencies>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
        <version>3.6.1</version>
    </dependency>
</dependencies>
~~~

