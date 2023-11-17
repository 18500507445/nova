# mybatis单独使用

```java
public class Test1 {

    public static void main(String[] args) throws IOException {

        //TODO 第一步：读取mybatis-config.xml配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
       
        //第二步：构建SqlSessionFactory (框架初始化) == new DefaultSqlSessionFactory持有一个Configuration(创建解析并放入)的引用;
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //第三步：打开SqlSession  创建一个执行链executor（数据源,jdbc事务Transaction）对象 持有Configuration 包含是否自动提交
        SqlSession session = sqlSessionFactory.openSession();

        //第四步：获取Mapper接口对象 (底层是动态代理) 创建代理对象 持有SqlSession
        AccountMapper accountMapper = session.getMapper(AccountMapper.class);

        //第五步：调用Mapper接口对象的方法操作数据库；
        Account account = accountMapper.selectByPrimaryKey(1);
      

        //第六步：业务处理
        log.info("查询结果>>: " + account.getIdCardType().getIdCard() + "--" + account.getRealname());

        //session提交并关闭
        session.commit();
        session.close();
    }
}
```

- 获取配置文件
- 解析配置文件将其放入到sqlSessionFactory的configuration
- 根据配置的信息创建事务，数据源，创建一个执行链 ，创建一个默认SqlSession对象
- 创建代理mapper接口的对象，持有SqlSession
- 判断是什么类型执行语句，查询语句获取预编译的StatementHandler解析SQL语句，然后根据数据源信息打开通道，创建操作对象，设置参数，对数据库进行操作获取结果，对结果的封装

# 自己简化的整合

## @Bean方式注册

1. 创建代理对象需要使用SqlSession尽心创建
2. 而使用SqlSession 需要一个 SqlSessionFactory

```java
@Bean
public SqlSessionFactory SqlSessionFactory(){
    //TODO 第一步：读取mybatis-config.xml配置文件
    InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");

    //第二步：构建SqlSessionFactory (框架初始化) == new DefaultSqlSessionFactory持有一个Configuration(创建解析并放入)的引用;
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    return sqlSessionFactory;
}

@Bean
public AccountMapper accountMapper(SqlSessionFactory sqlSessionFactory){
    // 没有在配置文件中指定
    sqlSessionFactory.getConfiguration.addMapper(AccountMapper.class));
    
    //第三步：打开SqlSession  创建一个执行链executor（数据源,jdbc事务Transaction）对象 持有Configuration 包含是否自动提交
    SqlSession session = sqlSessionFactory.openSession();

    //第四步：获取Mapper接口对象 (底层是动态代理) 创建代理对象 持有SqlSession
    AccountMapper accountMapper = session.getMapper(AccountMapper.class);

    return accountMapper;
}
```

```xml
<!--配置mybatis工厂bean：SqlSessionFactoryBean-->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!--此文件mybatis-config.xml在整合spring开发的时候，也可以省略，那么mybatis就是默认配置-->
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
    <!--如果你的Mapper接口和Mapper.xml不在一个包下，那么需要指定Mapper.xml的位置-->
    <property name="mapperLocations" value="classpath:mapper/*.xml"/>
</bean>

<!--配置mybatis mapper包扫描-->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <!--扫描mapper包，把mapper包下的mapper接口注册为spring的bean定义 MapperFactoryBean-->
    <!--扫描出来的每一个mapper接口，会变成MapperFactoryBean对象，MapperFactoryBean对象依赖sqlSessionFactory-->
    <property name="basePackage" value="com.bjpowernode.mapper"/>
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
</bean>
```

## FactoryBean注册代理类

使用FactoryBean的getObject()方法注册代理类

```java
@Component
public class MyFactoryBean implements FactoryBean {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Object getObject() throws Exception {
        // 没有在配置文件中指定
        sqlSessionFactory.getConfiguration.addMapper(AccountMapper.class));

        //第三步：打开SqlSession  创建一个执行链executor（数据源,jdbc事务Transaction）对象 持有Configuration 包含是否自动提交
        SqlSession session = sqlSessionFactory.openSession();

        //第四步：获取Mapper接口对象 (底层是动态代理) 创建代理对象 持有SqlSession

        return session.getMapper(AccountMapper.class);
    }

    @Override
    public Class<?> getObjectType() {
        return AccountMapper.class;
    }
}
```

## 多个Mapper创建对象

使用FactoryBean的有参构造使得接口灵活

```java
public class MyFactoryBean implements FactoryBean {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	private Class mapperClass;

	public MyFactoryBean(Class mapperClass) {
		this.mapperClass = mapperClass;
	}

	@Override
	public Object getObject() throws Exception {
		// 没有在配置文件中指定
		sqlSessionFactory.getConfiguration.addMapper(mapperClass));

		//第三步：打开SqlSession  创建一个执行链executor（数据源,jdbc事务Transaction）对象 持有Configuration 包含是否自动提交
		SqlSession session = sqlSessionFactory.openSession();

		//第四步：获取Mapper接口对象 (底层是动态代理) 创建代理对象 持有SqlSession

		return session.getMapper(mapperClass);
	}

	@Override
	public Class<?> getObjectType() {
		return mapperClass;
	}
}
```

- main方法

利用spring  创建类型为MyFactoryBean.class的beanDefinition 指定他的有参构造为需要代理的mapper.class

可以使用for进行循环

```java
public class Test {

	public static void main(String[] args) throws Exception {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class))

            
		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
		beanDefinition.setBeanClass(MyFactoryBean.class);
		beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(AccountMapper.class);
		context.registerBeanDefinition("accountMapper",beanDefinition);
	
	}
}
```

## 使用beanDefinitionRegistrar进行注册

```java
public class MyBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
		beanDefinition.setBeanClass(MyFactoryBean.class);
		beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(AccountMapper.class);
		registry.registerBeanDefinition("accountMapper",beanDefinition);
	}
}
// 导入类
```

## 使用ClassPathBeanDefinitionScanner更改扫描路径

- 更改spring默认的包扫描 使得接口也可以扫描进入
- 将接口变为beanDefinition 他默认的类不是我们的指定的**FactoryBean**
- FactoryBean 使用它的有参构造将类放入 调用它的方法创建代理类
- 这就是偷天换日

```java
public class MyMapperScanner extends ClassPathBeanDefinitionScanner {


    //判断是否有@Component
    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        return true;
    }

    //是否是个接口
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }

    public MyMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    // 将扫描到的变为BeanDefinitionHolder
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {

        // 获取到扫描到的beanDefinition 更改他们的类（doScan会将beanDefinition放入到容器中）
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClass());

            beanDefinition.setBeanClass(MyFactoryBean.class);

        }

        return beanDefinitionHolders;
    }
}

```

```java
public class MyBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        // 创建我们自己的包扫描规则
        MyMapperScanner myMapperScanner = new MyMapperScanner(registry);
        // 放入到beanFactory
        myMapperScanner.scan("com.cs.mapper");
        
       
    }
}
```

## 自定义包扫描注解

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MyBeanDefinitionRegistrar.class)
public @interface MyMapperScan {

	String value();
}
//放到配置类上 MyBeanDefinitionRegistrar 会进行调用
```

```java
public class MyBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {


	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {


		Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(MyMapperScanner.class);
		MyMapperScanner myMapperScanner = new MyMapperScanner(registry);
		myMapperScanner.scan(String.valueOf(annotationAttributes.get("value")));
	}
}
```

## 总结

- 使用**@MyMapperScan**放入需要扫描的包
- 导入 **MyBeanDefinitionRegistrar** 进行注册beanDefinition 
  - 创建**类路径Bean定义扫描器** 通过获取@MyMapperScan 注解信息获取我们的扫描路径
  - 更改我们自己的**过滤规则** **创建beanDefinition** 并放入到BeanFactory
  - 由于需要进行代理，因此我们需要自己创建beanDefinition对象

- 使用FactoryBean的方法进行bean代理的创建
  - 由于使用FactoryBean的有参构造进行创建传递需要创建的bean的class
  - 因此在**类路径Bean定义扫描器** 需要更改**beanDefintion的beanClass**（偷天换日）

- 创建代理对象需要使用容器中有**SqlSessionFactory**

# 源码解析

## @MapperScan

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MapperScannerRegistrar.class)
@Repeatable(MapperScans.class)
public @interface MapperScan {

  String[] value() default {};

  String[] basePackages() default {};

  Class<?>[] basePackageClasses() default {};

  Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;


  Class<? extends Annotation> annotationClass() default Annotation.class;


  Class<?> markerInterface() default Class.class;

  String sqlSessionTemplateRef() default "";


  String sqlSessionFactoryRef() default "";


  Class<? extends MapperFactoryBean> factoryBean() default MapperFactoryBean.class;


  String lazyInitialization() default "";

}
```

## @Import(MapperScannerRegistrar.class)

```java
@Import(MapperScannerRegistrar.class)
```

```java
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
     // 获取MapperScan注解信息
    AnnotationAttributes mapperScanAttrs = AnnotationAttributes
        .fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
    if (mapperScanAttrs != null) {
      registerBeanDefinitions(mapperScanAttrs, registry, generateBaseBeanName(importingClassMetadata, 0));
    }
  }

  void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry, String beanName) {
	// 创建MapperScannerConfigurer类的Bean定义。
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
    builder.addPropertyValue("processPropertyPlaceHolders", true);

    Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
    if (!Annotation.class.equals(annotationClass)) {
      builder.addPropertyValue("annotationClass", annotationClass);
    }

    Class<?> markerInterface = annoAttrs.getClass("markerInterface");
    if (!Class.class.equals(markerInterface)) {
      builder.addPropertyValue("markerInterface", markerInterface);
    }

    Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
    if (!BeanNameGenerator.class.equals(generatorClass)) {
      builder.addPropertyValue("nameGenerator", BeanUtils.instantiateClass(generatorClass));
    }

    Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
    if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
      builder.addPropertyValue("mapperFactoryBeanClass", mapperFactoryBeanClass);
    }

    String sqlSessionTemplateRef = annoAttrs.getString("sqlSessionTemplateRef");
    if (StringUtils.hasText(sqlSessionTemplateRef)) {
      builder.addPropertyValue("sqlSessionTemplateBeanName", annoAttrs.getString("sqlSessionTemplateRef"));
    }

    String sqlSessionFactoryRef = annoAttrs.getString("sqlSessionFactoryRef");
    if (StringUtils.hasText(sqlSessionFactoryRef)) {
      builder.addPropertyValue("sqlSessionFactoryBeanName", annoAttrs.getString("sqlSessionFactoryRef"));
    }

    List<String> basePackages = new ArrayList<>();
    basePackages.addAll(
        Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));

    basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
        .collect(Collectors.toList()));

    basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
        .collect(Collectors.toList()));

    String lazyInitialization = annoAttrs.getString("lazyInitialization");
    if (StringUtils.hasText(lazyInitialization)) {
      builder.addPropertyValue("lazyInitialization", lazyInitialization);
    }

    builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
	// 加入到beanDefinition
    registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

  }
```

## MapperScannerConfigurer.class

实现了BeanDefinitionRegistryPostProcessor 进行后置处理

可以看到一样使用了类路径Bean定义扫描器

```java
public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
    if (this.processPropertyPlaceHolders) {
      processPropertyPlaceHolders();
    }

    ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
    scanner.setAddToConfig(this.addToConfig);
    scanner.setAnnotationClass(this.annotationClass);
    scanner.setMarkerInterface(this.markerInterface);
    scanner.setSqlSessionFactory(this.sqlSessionFactory);
    scanner.setSqlSessionTemplate(this.sqlSessionTemplate);
    scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
    scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
    scanner.setResourceLoader(this.applicationContext);
    scanner.setBeanNameGenerator(this.nameGenerator);
    scanner.setMapperFactoryBeanClass(this.mapperFactoryBeanClass);
    if (StringUtils.hasText(lazyInitialization)) {
      scanner.setLazyInitialization(Boolean.valueOf(lazyInitialization));
    }
    scanner.registerFilters();
    scanner.scan(
        StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
  }
```

## ClassPathMapperScanner

重新更改创建beanDefinition规则

更改beanDefinition的beanClass

```java
public Set<BeanDefinitionHolder> doScan(String... basePackages) {
    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

    if (beanDefinitions.isEmpty()) {
        LOGGER.warn(() -> "No MyBatis mapper was found in '" + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
    } else {
        processBeanDefinitions(beanDefinitions);
    }

    return beanDefinitions;
}

private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
    GenericBeanDefinition definition;
    for (BeanDefinitionHolder holder : beanDefinitions) {
        definition = (GenericBeanDefinition) holder.getBeanDefinition();
        String beanClassName = definition.getBeanClassName();
        LOGGER.debug(() -> "Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName
                     + "' mapperInterface");

        // the mapper interface is the original class of the bean
        // but, the actual class of the bean is MapperFactoryBean
        definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName); // issue #59
        definition.setBeanClass(this.mapperFactoryBeanClass);

        definition.getPropertyValues().add("addToConfig", this.addToConfig);

        boolean explicitFactoryUsed = false;
        if (StringUtils.hasText(this.sqlSessionFactoryBeanName)) {
            definition.getPropertyValues().add("sqlSessionFactory",
                                               new RuntimeBeanReference(this.sqlSessionFactoryBeanName));
            explicitFactoryUsed = true;
        } else if (this.sqlSessionFactory != null) {
            definition.getPropertyValues().add("sqlSessionFactory", this.sqlSessionFactory);
            explicitFactoryUsed = true;
        }

        if (StringUtils.hasText(this.sqlSessionTemplateBeanName)) {
            if (explicitFactoryUsed) {
                LOGGER.warn(
                    () -> "Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
            }
            definition.getPropertyValues().add("sqlSessionTemplate",
                                               new RuntimeBeanReference(this.sqlSessionTemplateBeanName));
            explicitFactoryUsed = true;
        } else if (this.sqlSessionTemplate != null) {
            if (explicitFactoryUsed) {
                LOGGER.warn(
                    () -> "Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
            }
            definition.getPropertyValues().add("sqlSessionTemplate", this.sqlSessionTemplate);
            explicitFactoryUsed = true;
        }

        if (!explicitFactoryUsed) {
            LOGGER.debug(() -> "Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.");
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
        definition.setLazyInit(lazyInitialization);
    }
}
```

## 为什么使用MapperScannerConfigurer.class

- 我们可以自己创建一个MapperScannerConfigurer的bean

```java
@Bean
public MapperScannerConfigurer mapperScannerConfigurer(){
    MapperScannerConfigurer configurer = new MapperScannerConfigurer();
    configurer.setBasePackage("com.cs.mapper");
    return configurer;
}
```

- 不使用MapperScanner进行包扫描

- 在springBoot中 自动配置中装配了这个类
  - 使用**@Mapper**，对我们的接口进行限制，含有此接口的才能创建代理对象

- springboot 的两套创建mybatis的代理对象



