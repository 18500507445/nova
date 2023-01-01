### 集成JUnit测试
Spring为我们提供了一个Test模块，它会自动集成Junit进行测试，我们可以导入一下依赖：
~~~xml
<dependencies>
    <!-- 单元测试 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
    </dependency>

    <!-- spring test -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
    </dependency>
</dependencies>
~~~

这里导入的是JUnit5和SpringTest模块依赖，然后直接在我们的测试类上添加两个注解就可以搞定：
~~~java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MybatisConfiguration.class)
public class MybatisTests {
    @Resource
    public StudentMapper studentMapper;
}
~~~

`@ExtendWith`是由JUnit提供的注解，等同于旧版本的`@RunWith`注解，然后使用SpringTest模块提供的`@ContextConfiguration`注解来表示要加载哪一个配置文件，可以是XML文件也可以是类，我们这里就直接使用类进行加载。

配置完成后，我们可以直接使用`@Autowired`来进行依赖注入，并且直接在测试方法中使用注入的Bean，现在就非常方便了。