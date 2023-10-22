## nova-mybatis-plus
### 简介:
* [Mybatisplus学习](https://baomidou.com/)
* [学习视频](https://www.bilibili.com/video/BV1Bc411W7Wj?p=18&vd_source=bf69afcaca624e3b61a8f265c720a96b)
* [参考笔记](https://blog.csdn.net/ewertyucf/article/details/130106739)
* [细讲配置参数](https://yixinforu.blog.csdn.net/article/details/122562231?spm=1001.2014.3001.5502)
* [常用注解](https://yixinforu.blog.csdn.net/article/details/122516135)
* PageHelper源码核心类PageInterceptor（日志开关、拦截器、count查询）

### 第一章：入门篇（sql文件在resources路径下）
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
  #打印sql-log
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
* 主键策略
  ~~~java
  import com.baomidou.mybatisplus.annotation.IdType;
  
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;
  ~~~
  (1)AUTO策略，Mysql数据库设置Id自增  
  (2)INPUT策略，手动setId   
  (3)ASSIGN_ID，雪花算法 
  (4)ASSIGN_UUID，UUID 
  (5)NONE或者不添加注解，走全局配置，全局默认走雪花算法  
  ~~~yml
  mybatis-plus:
  global-config:
    #全局默认雪花算法
    db-config:
      id-type: assign_id
  ~~~
* 分页（分页插件、自定义分页插件）  
  (1)添加配置类-MybatisPlusConfig  

* ActiveRecord-领域模型（测试方法-activeRecordInsert）  
  (1)DO extends Model<T>  
  (2)对应的mapper也需要有  

* SimpleQuery工具类（测试方法-simpleQueryList）  
  (1)可以对selectList查询后结用Stream进行封装，返回一些指定结果，简洁api调用  

### 第五章：高级篇
* 逻辑删除（更新status字段）  
  ~~~java
  /**
   * 删除表示 默认0
   */
  @TableLogic(value = "0", delval = "1")
  private Integer status;
  ~~~
  
* 通用枚举  
  ~~~java
  @EnumValue
  private final Integer gender;
  ~~~
  
* 字段类型处理器（map转Json存入表， 需要fastJson依赖） 
  ~~~java
  @TableName(autoResultMap = true)
  
  /**
   * 联系方式，字段处理成Json
   */
  @TableField(typeHandler = FastjsonTypeHandler.class)
  private Map<String, String> contact;
  ~~~
  
* 自动填充（时间自动更新，了解就行了，我自己习惯设计数据库的时候字段设置好自动更新）  
  设置处理器，MyMetaHandler  
  ~~~java
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;
  ~~~
  
* 防止全表更新插件，配置一个拦截器（MybatisPlusConfig） 
  ~~~java
  interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
  ~~~

* 支持乐观锁注解，配置文件添加插件  
  ~~~java
  @Version
  private Integer version;
  ~~~
  
  ~~~java
  interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
  ~~~
  
* 代码生成器（Chapter5Test.main方法）
  ~~~xml
  <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-generator</artifactId>
  </dependency>
  
  <dependency>
      <groupId>org.freemarker</groupId>
      <artifactId>freemarker</artifactId>
  </dependency>
  ~~~

* 多数据源配置（修改yml文件，@DS("tableName")可以作用于ServiceImpl、Mapper）
  ~~~xml
  <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
      <version>${dynamic.version}</version>
  </dependency>
  ~~~
  
* [开启事务](https://blog.csdn.net/qq_16159433/article/details/127223910?spm=1001.2014.3001.5502)
> @DsTransactional: 多数据源事务的开启注解，用法同@Transactional相同。但是不能指定事务的一些属性，
> 因为其实现的原理，也不需要任何其他的事务的配置。当发生任何Exception时都会执行回滚


