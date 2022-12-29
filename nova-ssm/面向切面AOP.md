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