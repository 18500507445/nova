### 使用Spring事务管理
现在我们来学习一下Spring提供的事务管理（Spring事务管理分为编程式事务和声明式事务，但是编程式事务过于复杂并且具有高度耦合性，  
违背了Spring框架的设计初衷，因此这里只讲解声明式事务）声明式事务是基于AOP实现的。

使用声明式事务非常简单，我们只需要在配置类添加`@EnableTransactionManagement`注解即可，  
这样就可以开启Spring的事务支持了。接着，我们只需要把一个事务要做的所有事情封装到Service层的一个方法中即可，首先需要在配置文件中注册一个新的Bean，事务需要执行必须有一个事务管理器：

~~~java
@Configuration
@MapperScan("com.nova.mybatis.mapper")
@ComponentScan("com.nova.mybatis.service")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class MybatisConfiguration {
   
    @Bean
    public DataSource dataSource() {
        return new PooledDataSource("com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://47.100.174.176:3306/study", "root", "@wangzehui123");
    }
  
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean;
    }

    @Bean
    public TransactionManager transactionManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
~~~

编写实现类
~~~java
@Component
public class StudentServiceImpl implements StudentService {

    @Resource
    StudentMapper studentMapper;
    @Transactional
    @Override
    public void insertTransactional() {
        studentMapper.insertStudent();
        if (true) {
            throw new RuntimeException("我是异常！");
        }
        studentMapper.insertStudent();
    }
}
~~~

测试类
~~~java
public class MybatisTests {

    @Resource
    public StudentService studentService;

    /**
     * 插入事务
     */
    @Test
    public void insert(){
        studentService.insertTransactional();
    }
}
~~~

执行抛出异常
>org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119)
org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:215)


我们发现，整个栈追踪信息中包含了大量aop包下的相关内容，也就印证了，它确实是通过AOP实现的，那么我们接着来看一下，数据库中的数据是否没有发生变化（出现异常回滚了）

结果显而易见，确实被回滚了，数据库中没有任何的内容。

我们接着来研究一下@Transactional注解的一些参数：

~~~java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {
    @AliasFor("transactionManager")
    String value() default "";

    @AliasFor("value")
    String transactionManager() default "";

    String[] label() default {};

    Propagation propagation() default Propagation.REQUIRED;

    Isolation isolation() default Isolation.DEFAULT;

    int timeout() default -1;

    String timeoutString() default "";

    boolean readOnly() default false;

    Class<? extends Throwable>[] rollbackFor() default {};

    String[] rollbackForClassName() default {};

    Class<? extends Throwable>[] noRollbackFor() default {};

    String[] noRollbackForClassName() default {};
}
~~~

我们来讲解几个比较关键的信息：

● transactionManager：指定事务管理器
● propagation：事务传播规则，一个事务可以包括N个子事务
● isolation：事务隔离级别，幻读、虚读、可重复读
● timeout：事务超时时间
● readOnly：是否为只读事务，不同的数据库会根据只读属性进行优化，比如MySQL一旦声明事务为只读，那么久不允许增删改操作了。
● rollbackFor和noRollbackFor：发生指定异常时回滚或是不回滚，默认发生任何异常都回滚

除了事务的传播规则，其他的内容其实已经给大家讲解过了，那么我们就来看看事务的传播。事务传播一共有七种级别：
![事务隔离级别](https://www.yuque.com/api/filetransfer/images?url=https%3A%2F%2Fimg-blog.csdn.net%2F20170420212829825%3Fwatermark%2F2%2Ftext%2FaHR0cDovL2Jsb2cuY3Nkbi5uZXQvc29vbmZseQ%3D%3D%2Ffont%2F5a6L5L2T%2Ffontsize%2F400%2Ffill%2FI0JBQkFCMA%3D%3D%2Fdissolve%2F70%2Fgravity%2FSouthEast&sign=654f7dc210afbca9621a8eebc6a7613f9b600e9e1b9b3c2eb3a46c846ecf0ce5)

Spring默认的传播级别是PROPAGATION_REQUIRED，那么我们来看看，它是如何传播的，现在我们的Service类中一共存在两个事务，而一个事务方法包含了另一个事务方法：
~~~java
public class StudentServiceImpl implements StudentService {

    @Resource
    StudentMapper studentMapper;

    /**
     * Transactional就相当于
     * DataSourceTransactionManager manager = new DataSourceTransactionManager();
     * try manager.commit()
     * catch manager.rollback()
     *
     * 抛出异常 一个数据都进不到数据库，因为事务要么都完成，要么都不完成
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertTransactional() {
        studentMapper.insertStudent();
        if (true) {
            throw new RuntimeException("我是insertTransactional异常！");
        }
        studentMapper.insertStudent();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOne() {
        studentMapper.insertStudent();
        insertTwo();
    }

    /**
     * 事务的隔离级别默认：PROPAGATION_REQUIRED
     * 也就是说insertTwo和insertOne都有事物，one里调用two，那么事物2会加入到事物1里
     * 执行结果都不插入
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertTwo() {
        studentMapper.insertTwo();
        throw new RuntimeException("我是insertTwo异常！");
    }

    /**
     * 事务的隔离级别：SUPPORTS（不需要上下文）
     * 单独调用insertThree方法，并不会以事务的方式执行
     * 当发生异常时，虽然依然存在AOP增强，但是不会进行回滚操作
     * 而现在再调用insertOne方法，才会以事务的方式执行。
     */
    @Transactional(propagation = Propagation.SUPPORTS ,rollbackFor = Exception.class)
    @Override
    public void insertThree() {
        studentMapper.insertThree();
        throw new RuntimeException("我是insertThree异常！");
    }

    /**
     * 事务的隔离级别：MANDATORY 如果当前方法并没有在任何事务中进行，会直接出现异常
     * 解释：如果insertFour不在insertTwo事务中直接抛出异常
     * No existing transaction found for transaction marked with propagation 'mandatory'
     */
    @Transactional(propagation = Propagation.MANDATORY ,rollbackFor = Exception.class)
    @Override
    public void insertFour() {
        studentMapper.insertFour();
        throw new RuntimeException("我是insertFour异常！");
    }
}
~~~

spring事务失效的场景
* 方法内的自调用
* 方法是private的
* 方法是final的
* 单独的线程调用方法
* 异常被吃掉
* 类没有被spring管理