#### 第一个Spring项目
##### 我们创建一个新的Maven项目，并导入Spring框架的依赖，Spring框架的坐标：
~~~xml
 <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
</dependency>
~~~
##### 接着在resource中创建一个Spring配置文件，applicationContext-beans.xml，直接右键点击即可创建：
~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

</beans>
~~~
#### 最后，在主方法中编写：
~~~java
public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-beans.xml");
    }
}
~~~
#### 这样，一个最基本的Spring项目就创建完成了，接着我们来看看如何向IoC容器中注册JavaBean，首先创建一个Student类：
~~~java
//注意，这里还用不到值注入，只需要包含成员属性即可，不用Getter/Setter。
public class Student {
    String name;
    int age;
}
~~~
#### 然后在配置文件中添加这个bean：
~~~xml
<bean name="student" class="com.test.bean.Student"/>
~~~
#### 现在，这个对象不需要我们再去生成了，而是由IoC容器来提供，实际上，这里得到的Student对象是由Spring通过反射机制帮助我们创建的
~~~java
public class Main {
    @Test
    public void testIoC() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-beans.xml");
        
        Student student = context.getBean(Student.class);
        Student student2 = (Student) context.getBean("student");

        System.out.println("student = " + student);
        System.out.println("student2 = " + student3);
    }
}
~~~
#### 我们发现两次获取到的实际上是同一个对象，也就是说，默认情况下，通过IoC容器进行管理的JavaBean是单例模式(饿汉式)，无论怎么获取始终为那一个对象，那么如何进行修改呢？只需要修改其作用域即可，添加scope属性：
~~~xml
<bean name="student" class="com.test.bean.Student" scope="prototype"/>
~~~
#### 通过将其设定为prototype（原型模式）来使得其每次都会创建一个新的对象。我们接着来观察一下，这两种模式下Bean的生命周期，构造方法添加一个输出：
~~~java
public class Student {
    String name;
    int age;

    public Student(){
        System.out.println("我是student！");
    }
}
~~~
#### 接着我们在main方法中打上断点来查看对象分别是在什么时候被构造的。

我们发现，当Bean的作用域为单例模式，那么它会在一开始就被创建，而处于原型模式下，只有在获取时才会被创建，也就是说，单例模式下，Bean会被IoC容器存储，只要容器没有被销毁，那么此对象将一直存在，而原型模式才是相当于直接new了一个对象，并不会被保存。

我们还可以通过配置文件，告诉创建一个对象需要执行此初始化方法，以及销毁一个对象的销毁方法：
~~~java
public class Student {
    String name;
    int age;

    private void init(){
        System.out.println("我是初始化方法！");
    }

    private void destroy(){
        System.out.println("我是销毁方法！");
    }

    public Student(){
        System.out.println("我是student！");
    }
}
~~~
#### 最后在XML文件中编写配置：
~~~xml
<bean name="student" class="com.test.bean.Student" init-method="init" destroy-method="destroy"/>
~~~

#### 我们还可以手动指定Bean的加载顺序，若某个Bean需要保证一定在另一个Bean加载之前加载，那么就可以使用depend-on属性
~~~xml
<!-- scope="prototype" 多例模式 每次new() 是不同的一个对象 -->
<bean name="card" class="com.nova.ssm.entity.Card" scope="prototype" depends-on="student">

</bean>
~~~
### 依赖注入DI
#### 我们已经了解如何注册和使用一个Bean，那么，如何向Bean的成员属性进行赋值呢？IoC在创建对象时，需要将我们预先给定的属性注入到对象中，非常简单，我们可以使用property标签来实现，但是一定注意，此属性必须存在一个set方法，否则无法赋值：
~~~java
public class Main {
    @Test
    public void testDi() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-beans.xml");
        Student student = context.getBean(Student.class);
        System.out.println("student=" + student);
        student.say();
    }
}
~~~
#### 我们还可以使用自动装配来实现属性值的注入：
~~~xml
<bean name="student" class="com.test.bean.Student" autowire="byType"/>
~~~

#### 构造注入
~~~xml
<bean name="teacher" class="com.nova.ssm.entity.Teacher">
    <constructor-arg index="0" type="java.lang.String" value="李老师"/>
    <constructor-arg index="1" type="java.lang.Integer" value="18"/>
</bean>
~~~