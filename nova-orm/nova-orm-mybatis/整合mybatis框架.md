### 整合Mybatis框架
通过了解数据源，我们已经清楚，Mybatis实际上是在使用自己编写的数据源（数据源有很多，之后我们再聊其他的）默认使用的是池化的数据源，它预先存储了很多的连接对象。

那么我们来看一下，如何将Mybatis与Spring更好的结合呢，比如我们现在希望将SqlSessionFactory交给IoC容器进行管理，而不是我们自己创建工具类来管理（我们之前一直都在使用工具类管理和创建会话）

首先导入依赖：

~~~xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
    </dependency>

    <!-- Mysql驱动包 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <!-- mybatis (和父类pom spring-mybatis-starter里 mybatis版本保持一致)-->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.9</version>
    </dependency>

    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
    </dependency>

    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>2.0.7</version>
    </dependency>
</dependencies>
~~~

在mybatis-spring依赖中，为我们提供了SqlSessionTemplate类，  
它其实就是官方封装的一个工具类，我们可以将其注册为Bean，  
这样我们随时都可以向IoC容器索要，而不用自己再去编写一个工具类了，  
我们可以直接在配置类中创建：

~~~java
@Configuration
@MapperScan("com.nova.mybatis.mapper")
public class MybatisConfiguration {
    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws IOException {
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
        return new SqlSessionTemplate(factory);
    }
}
~~~
mybatis-config.xml配置
~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://47.100.174.176:3306/study"/>
                <property name="username" value="root"/>
                <property name="password" value="password"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper class="com.nova.mybatis.mapper.StudentMapper"/>
    </mappers>
</configuration>
~~~

实体类，mapper接口，测试类
~~~java
@Data
public class Student {

    private Long id;

    private String name;

    private int age;

}

@Mapper
public interface TestMapper {

    @Select("select * from student where sid = 1")
    Student getStudent();
}

public class MybatisTests {
    @Test
    public void testXml() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MybatisConfiguration.class);
        SqlSessionTemplate template = context.getBean(SqlSessionTemplate.class);
        StudentMapper studentMapper = template.getMapper(StudentMapper.class);
        System.out.println(studentMapper.getStudent());
    }
}
~~~

我们接着来看，如果我们希望直接去除Mybatis的配置文件，  
那么改怎么去实现呢？我们可以使用SqlSessionFactoryBean类：
~~~java
@Configuration
@MapperScan("com.nova.mybatis.mapper")
public class MybatisConfiguration {
   
    @Bean
    public DataSource dataSource(){
        return new PooledDataSource("com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://47.100.174.176:3306/study", "root", "password");
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired DataSource dataSource){
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean;
    }

}   
~~~

首先我们需要创建一个数据源的实现类，因为这是数据库最基本的信息，然后再给到SqlSessionFactoryBean实例，  
这样，我们相当于直接在一开始通过IoC容器配置了SqlSessionFactory，只需要传入一个DataSource的实现即可。

删除配置文件，重新再来运行，同样可以正常使用Mapper。从这里开始，通过IoC容器，  
Mybatis已经不再需要使用配置文件了，之后基于Spring的开发将不会再出现Mybatis的配置文件。