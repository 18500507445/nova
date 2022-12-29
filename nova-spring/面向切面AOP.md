#### 面向切面AOP
#### 又是一个听起来很高大上的名词，AOP思想实际上就是：在运行时，动态地将代码切入到类的指定方法、指定位置上的编程思想就是面向切面的编程。也就是说，我们可以使用AOP来帮助我们在方法执行前或执行之后，做一些额外的操作，实际上，就是代理！
~~~xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
</dependency>
~~~
####那么，如何使用AOP呢？首先我们要明确，要实现AOP操作，我们需要知道这些内容：
1. 需要切入的类，类的哪个方法需要被切入
2. 切入之后需要执行什么动作
3. 是在方法执行前切入还是在方法执行后切入
4. 如何告诉Spring需要进行切入
#### 我们依次来看，首先需要解决的问题是，找到需要切入的类：
~~~java
public class People {
    public int say(String str) {
        System.out.println("我想说：" + str);
        return str.length();
    }
}
~~~

#### 通过使用aop:config来添加一个新的AOP配置：

~~~xml

<aop:config>
    <aop:aspect ref="aopTest">
        <aop:pointcut id="say" expression="execution(* com.nova.spring.entity.People.say(String))"/>
        <!--        <aop:before method="before" pointcut-ref="say"/>-->
        <!--        <aop:after-returning method="after" pointcut-ref="say"/>-->
        <aop:around method="around" pointcut-ref="say"/>

    </aop:aspect>

    <!--    <aop:advisor advice-ref="aopAdvice" pointcut-ref="say"/>-->

    <aop:aspect ref="aopTest">
        <aop:pointcut id="test" expression="@annotation(Deprecated)"/>
        <aop:after-returning method="before" pointcut-ref="test"/>
    </aop:aspect>
</aop:config>
~~~

#### 其中，expression属性的execution填写格式如下：
* 修饰符：public、protected、private、包括返回值类型、static等等（使用*代表任意修饰符）
* 包名：如com.test（代表全部，比如com.代表com包下的全部包）
* 类名：使用*也可以代表包下的所有类
* 方法名称：可以使用*代表全部方法
* 方法参数：填写对应的参数即可，比如(String, String)，也可以使用*来代表任意一个参数，使用..代表所有参数。

### 使用接口实现AOP
####其实，我们之前学习的操作正好对应了AOP 领域中的特性术语：
* 通知（Advice）: AOP 框架中的增强处理，通知描述了切面何时执行以及如何执行增强处理，也就是我们上面编写的方法实现。
* 连接点（join point）: 连接点表示应用执行过程中能够插入切面的一个点，这个点可以是方法的调用、异常的抛出，实际上就是我们在方法执行前或是执行后需要做的内容。
* 切点（PointCut）: 可以插入增强处理的连接点，可以是方法执行之前也可以方法执行之后，还可以是抛出异常之类的。
* 切面（Aspect）: 切面是通知和切点的结合，我们之前在xml中定义的就是切面，包括很多信息。
* 引入（Introduction）：引入允许我们向现有的类添加新的方法或者属性。
* 织入（Weaving）: 将增强处理添加到目标对象中，并创建一个被增强的对象，我们之前都是在将我们的增强处理添加到目标对象，也就是织入