### 深入Mybatis框架
学习了Spring之后，我们已经了解如何将一个类作为Bean交由IoC容器管理，也就是说，现在我们可以通过更方便的方式来使用Mybatis框架，  
我们可以直接把SqlSessionFactory、Mapper交给Spring进行管理， 并且可以通过注入的方式快速地使用它们。  
因此，我们要学习一下如何将Mybatis与Spring进行整合，那么首先，我们需要在之前知识的基础上继续深化学习。

#### 了解数据源
在之前，我们如果需要创建一个JDBC的连接，那么必须使用DriverManager.getConnection()来创建连接，连接建立后，我们才可以进行数据库操作。
而学习了Mybatis之后，我们就不用再去使用DriverManager为我们提供连接对象，而是直接使用Mybatis为我们提供的SqlSessionFactory工具类来获取对应的SqlSession通过会话对象去操作数据库。
那么，它到底是如何封装JDBC的呢？我们可以试着来猜想一下，会不会是Mybatis每次都是帮助我们调用DriverManager来实现的数据库连接创建？我们可以看看Mybatis的源码：

源码SqlSessionFactory接口
~~~java
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;
   
    @Override
    public SqlSession openSession(Connection connection) {
        return openSessionFromConnection(configuration.getDefaultExecutorType(), connection);
    }

    private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
            tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
            final Executor executor = configuration.newExecutor(tx, execType);
            return new DefaultSqlSession(configuration, executor, autoCommit);
        } catch (Exception e) {
            closeTransaction(tx); // may have fetched a connection so lets call close()
            throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
        } finally {
            ErrorContext.instance().reset();
        }
    }
}
~~~

#### 在通过SqlSessionFactory调用openSession方法之后，它调用了内部的一个私有的方法openSessionFromDataSource，我们接着来看，这个方法里面定义了什么内容：