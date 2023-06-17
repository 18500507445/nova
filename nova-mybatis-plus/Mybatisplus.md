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

* Mapper继承BaseMapper<T>
~~~java
@Mapper
public interface OneUserMapper extends BaseMapper<UserDO> {

}
~~~

### 第二章：基础篇
* 基础的增删改查

* 简化Service与实现类
~~~java
public interface TwoUserService extends IService<UserDO> {

}
~~~

~~~java
@Service
public class TwoUserServiceImpl extends ServiceImpl<TwoUserMapper, UserDO> implements TwoUserService {

}
~~~

* 自定义Mapper接口方法（传统的Mybatis写法一样）

### 第三章：进阶篇
* Mybatis-plus映射规则  
  (1)表名和实体类名字映射--> 表明user，实体类名User，不一致可以通过@TableName("user")来指定    
  (2)字段名和实体类属性名映射--> 字段名name，实体类属性名name（Mybatis-plus是按照getName 、setName这个Name转小写来匹配的）  
  (3)字段名下划线命名方式和实体类属性小驼峰命名方式映射--> 字段名user_mail，实体类属性名userMail  
  ~~~yml
  configuration:
    #数据库下划线命名映射小驼峰命名 user_name --> userName
    map-underscore-to-camel-case: true
  ~~~
  (4)表映射的全局配置，如下配置，sql查询表明会自动带入a_tableName  
  ~~~yml
  mybatis-plus:
  global-config:
    db-config:
      table-prefix: a_
  ~~~
  (5)自定义配置字段名和实体类属性名映射，@TableField("user_name")  
  (6)sql中属性是关键字，@TableField("`desc`")  
  (7)字段失效，@TableField(select = false)，默认是展示
  (8)视图属性，实体类字段，但是表中没有对应的字段，@TableField(exist = false)

* 条件构造器QueryWrapper，例子Chapter3Test

### 第四章：高级篇
  