

# spring-aop

InstantiationAwareBeanPostProcessor 与 BeanPostProcessor

- InstantiationAwareBeanPostProcessor： **在bean创建前进行调用** postProcessBeforeInstantiation且继承 BeanPostProcessor
- BeanPostProcessor： **在bean初始化前后进行调用** postProcessBeforeInitialization

# 1、注册 “支持注释的AspectJ自动代理生成器”

1. 主要使用**IOC**提供的扩展对类进行动态代理

2. **@EnableAspectJAutoProxy**   开启aop功能 主要导入了一个组件@Import(**AspectJAutoProxyRegistrar**.class)

   实现***ImportBeanDefinitionRegistrar*** 接口(会在ioc容器第5步扫描配置类中放入一个**beanDefinition**)

3. AspectJAutoProxyRegistrar会创建并给容器放入internalAutoProxyCreator==》**AnnotationAwareAspectJAutoProxyCreator**

4. AnnotationAwareAspectJAutoProxyCreator实现***InstantiationAwareBeanPostProcessor***与***BeanFactoryAware***

5. 在bean创建前创建代理对象

![AnnotationAwareAspectJAutoProxyCreator](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/nova/spring-aop/1.png)

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AspectJAutoProxyRegistrar.class)
public @interface EnableAspectJAutoProxy {
	boolean proxyTargetClass() default false;
	boolean exposeProxy() default false;
}
```

```java
class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {

	/**
	 * Register, escalate, and configure the AspectJ auto proxy creator based on the value
	 * of the @{@link EnableAspectJAutoProxy#proxyTargetClass()} attribute on the importing
	 * {@code @Configuration} class.
	 */
	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		//给容器中放入internalAutoProxyCreator==》AnnotationAwareAspectJAutoProxyCreator 组件
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);

		AnnotationAttributes enableAspectJAutoProxy =
				AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
		if (enableAspectJAutoProxy != null) {
			if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
				AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
			}
			if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
				AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
			}
		}
	}
}

public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
		return registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry, null);
}

public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(
			BeanDefinitionRegistry registry, @Nullable Object source) {
	//AnnotationAwareAspectJAutoProxyCreator
	return registerOrEscalateApcAsRequired(AnnotationAwareAspectJAutoProxyCreator.class, registry, source);
}

private static BeanDefinition registerOrEscalateApcAsRequired(
			Class<?> cls, BeanDefinitionRegistry registry, @Nullable Object source) {

    Assert.notNull(registry, "BeanDefinitionRegistry must not be null");

    if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
        BeanDefinition apcDefinition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
        if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
            int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
            int requiredPriority = findPriorityForClass(cls);
            if (currentPriority < requiredPriority) {
                apcDefinition.setBeanClassName(cls.getName());
            }
        }
        return null;
    }

    RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
    beanDefinition.setSource(source);
    beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
    beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    //AUTO_PROXY_CREATOR_BEAN_NAME "org.springframework.aop.config.internalAutoProxyCreator"
    //放入AnnotationAwareAspectJAutoProxyCreator
    registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
    return beanDefinition;
}
```

# 2、初始化支持注释的AspectJ自动代理生成器

- 在IOC的第六步会对所有的*BeanPostProcessor*进行初始化并放入到容器

会调用initBeanFactory方法将beanFactory放入

```java
protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    super.initBeanFactory(beanFactory);
    if (this.aspectJAdvisorFactory == null) {
        this.aspectJAdvisorFactory = new ReflectiveAspectJAdvisorFactory(beanFactory);
    }
    //用来找到所有的aspectJAdvisorsBuilder
    this.aspectJAdvisorsBuilder =
        new BeanFactoryAspectJAdvisorsBuilderAdapter(beanFactory, this.aspectJAdvisorFactory);
}
```

# 3、判断bean是否需要被代理

在IOC容器第11步创建bean

```java
try {
    //如果Bean配置了初始化前和初始化后的处理器，则试图返回一个需要创建Bean的代理对象
    //TODO 第一次调用bean的后置处理器 主要判断bean是否需要被代理  bean一般都为空
    //此时对象还没有实例化，只有 BeanDefinition  无法进行代理，只是将切面找出来进行缓存
    Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
    if (bean != null) {
        return bean;
    }
}

protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
    Object bean = null;
    if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
        // Make sure bean class is actually resolved at this point.
        // TODO mbd.isSynthetic() 表示是否合成类 hasInstantiationAwareBeanPostProcessors() 判断系统是否有InstantiationAwareBeanPostProcessors
        if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
            Class<?> targetType = determineTargetType(beanName, mbd);
            if (targetType != null) {
                //InstantiationAwareBeanPostProcessors 的前置处理器
                bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                if (bean != null) {
                    bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                }
            }
        }
        mbd.beforeInstantiationResolved = (bean != null);
    }
    return bean;
}
```

## - 调用实例化前的PostProcessors

```java
protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
    for (BeanPostProcessor bp : getBeanPostProcessors()) {
        if (bp instanceof InstantiationAwareBeanPostProcessor) {
            InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
            //调用postProcessBeforeInstantiation
            Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
            if (result != null) {
                return result;
            }
        }
    }
    return null;
}
```

```java
public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
	Object cacheKey = getCacheKey(beanClass, beanName);
	
	if (!StringUtils.hasLength(beanName) || !this.targetSourcedBeans.contains(beanName)) {
		// 已经被代理过则返回
		if (this.advisedBeans.containsKey(cacheKey)) {
			return null;
		}
		// 不应代理 或者 应该跳过
        //isInfrastructureClass是否是基础类型 Advice Pointcut Advisor AopInfrastructureBean 或者是否是切面（@Aspect）
        //TODO shouldSkip 是否需要跳过（不要处理）
		if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
            //不需要被代理会放入false
			this.advisedBeans.put(cacheKey, Boolean.FALSE);
			return null;
		}
	}
	//获取自定义目标源
	TargetSource targetSource = getCustomTargetSource(beanClass, beanName);
	if (targetSource != null) {
		if (StringUtils.hasLength(beanName)) {
			this.targetSourcedBeans.add(beanName);
		}
		// 获取所有合格的增强器
		Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
		// 创建 AOP 代理
		Object proxy = createProxy(beanClass, beanName, specificInterceptors, targetSource);
		// 存入缓存
		this.proxyTypes.put(cacheKey, proxy.getClass());
		return proxy;
	}

	return null;
}

```

### shouldSkip获取所有候选的切面的通知

```java
protected boolean shouldSkip(Class<?> beanClass, String beanName) {
    //查找所有候选的增强器 类型：InstantiationModelAwarePointcutAdvisor
    List<Advisor> candidateAdvisors = findCandidateAdvisors(); 
    for (Advisor advisor : candidateAdvisors) {
        //判断增强器是否是AspectJPointcutAdvisor类型
        if (advisor instanceof AspectJPointcutAdvisor &&
            ((AspectJPointcutAdvisor) advisor).getAspectName().equals(beanName)) {
            return true;
        }
    }
    return super.shouldSkip(beanClass, beanName);
}
//是否是原始类型的实例 super.shouldSkip(beanClass, beanName);
protected boolean shouldSkip(Class<?> beanClass, String beanName) {
    return AutoProxyUtils.isOriginalInstance(beanName, beanClass);
}
static boolean isOriginalInstance(String beanName, Class<?> beanClass) {
    //是否有长度 || 长度不等于 本来的name + AutowireCapableBeanFactory.ORIGINAL_INSTANCE_SUFFIX.length()
    if (!StringUtils.hasLength(beanName) || beanName.length() !=
        beanClass.getName().length() + AutowireCapableBeanFactory.ORIGINAL_INSTANCE_SUFFIX.length()) {
        return false;
    }
    return (beanName.startsWith(beanClass.getName()) &&
            beanName.endsWith(AutowireCapableBeanFactory.ORIGINAL_INSTANCE_SUFFIX));
}
```

#### 查找所有候选的增强器  findCandidateAdvisors（）

```java
protected List<Advisor> findCandidateAdvisors() {
    //调用父类方法从容器中查找所有的通知器
	//	从容器中查找所有类型为 Advisor 的 bean 对应的名称
	//	遍历 advisorNames，并从容器中获取对应的 bean
    List<Advisor> advisors = super.findCandidateAdvisors();
    // Build Advisors for all AspectJ aspects in the bean factory.
    if (this.aspectJAdvisorsBuilder != null) {
        //查找带有 @AspectJ 注解的 Aspect bean【不同的注解对应不同的切面类】【找 AspectJ 的 Advisor】 
        //aspectJAdvisorsBuilder 是之前回调的时候实例化的 BeanFactoryAspectJAdvisorsBuilderAdapte
        advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());
    }
    return advisors;
}

```

##### 1) 从容器中查找所有类型为 Advisor 的 bean 对应的名称

```java
protected List<Advisor> findCandidateAdvisors() {
    Assert.state(this.advisorRetrievalHelper != null, "No BeanFactoryAdvisorRetrievalHelper available");
   // 之前回调的时候实例化的 BeanFactoryAdvisorRetrievalHelperAdapter
    return this.advisorRetrievalHelper.findAdvisorBeans(); 
}
public List<Advisor> findAdvisorBeans() {
    // Determine list of advisor bean names, if not cached already. cachedAdvisorBeanNames 是 advisor 名称的缓存
    String[] advisorNames = this.cachedAdvisorBeanNames;
    //	如果 cachedAdvisorBeanNames 为空，这里到容器中查找，
    //	设置缓存，后续直接使用缓存即可
    if (advisorNames == null) {
        // Do not initialize FactoryBeans here: We need to leave all regular beans
        // uninitialized to let the auto-proxy creator apply to them! 从容器中查找 Advisor 类型 bean 的名称
        advisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
            this.beanFactory, Advisor.class, true, false);
        this.cachedAdvisorBeanNames = advisorNames;
    }
    if (advisorNames.length == 0) {
        return new ArrayList<>();
    }

    List<Advisor> advisors = new ArrayList<>();
    // 遍历 advisorNames
    for (String name : advisorNames) {
        if (isEligibleBean(name)) {
            // 忽略正在创建中的 advisor bean
            if (this.beanFactory.isCurrentlyInCreation(name)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Skipping currently created advisor '" + name + "'");
                }
            }
            else {
                try {
                    /*
					 * 调用 getBean 方法从容器中获取名称为 name 的 bean，
					 * 并将 bean 添加到 advisors 中
					 */
                    advisors.add(this.beanFactory.getBean(name, Advisor.class));
                }
                catch (BeanCreationException ex) {
                    Throwable rootCause = ex.getMostSpecificCause();
                    if (rootCause instanceof BeanCurrentlyInCreationException) {
                        BeanCreationException bce = (BeanCreationException) rootCause;
                        String bceBeanName = bce.getBeanName();
                        if (bceBeanName != null && this.beanFactory.isCurrentlyInCreation(bceBeanName)) {
                            if (logger.isTraceEnabled()) {
                                logger.trace("Skipping advisor '" + name +
                                             "' with dependency on currently created bean: " + ex.getMessage());
                            }
                            // Ignore: indicates a reference back to the bean we're trying to advise.
                            // We want to find advisors other than the currently created bean itself.
                            continue;
                        }
                    }
                    throw ex;
                }
            }
        }
    }
    return advisors;
}
```

##### 2) 查找带有 @AspectJ 注解的 Aspect bean

- 主要的逻辑就是根据注解类型生成与之对应的通知对象。
- 从目标 bean 中获取不包含 Pointcut 注解的方法列表
- 遍历上一步获取的方法列表，并调用 getAdvisor 获取当前方法对应的 Advisor
- 创建 AspectJExpressionPointcut 对象，并从方法中的注解中获取表达式，最后设置到切点对象
- 创建 Advisor 实现类对象 InstantiationModelAwarePointcutAdvisorImpl
- 调用 instantiateAdvice 方法构建通知
- 调用 getAdvice 方法，并根据注解类型创建相应的通知

```java
if (this.aspectJAdvisorsBuilder != null) { 
    //查找带有 AspectJ 注解的 Aspect bean【不同的注解对应不同的切面类】【找 AspectJ 的 Advisor】 
    //aspectJAdvisorsBuilder 是之前BeanPostProcessor实例化的 BeanFactoryAspectJAdvisorsBuilderAdapte
    advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());
}
```

```java
// 获取容器中所有 bean 的名称（beanName）
// 遍历上一步获取到的 bean 名称数组，并获取当前 beanName 对应的 bean 类型（beanType）
// 根据 beanType 判断当前 bean 是否是一个的 Aspect 注解类，若不是则不做任何处理
// 调用 advisorFactory.getAdvisors 获取通知器
public List<Advisor> buildAspectJAdvisors() {
     // 第一次会缓存，第二次直接获取
    List<String> aspectNames = this.aspectBeanNames;
    // 缓存未构建，则进行同步获取
    if (aspectNames == null) {
        synchronized (this) {
            aspectNames = this.aspectBeanNames;
            if (aspectNames == null) {
                List<Advisor> advisors = new ArrayList<>();
                aspectNames = new ArrayList<>(); 
                // 获取容器中所有组件的 beanName
                String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                    this.beanFactory, Object.class, true, false);
                for (String beanName : beanNames) { 
                    // 不合格则跳过
                    if (!isEligibleBean(beanName)) {
                        continue;
                    }
                    // We must be careful not to instantiate beans eagerly as in this case they
                    // would be cached by the Spring container but would not have been weaved.
                     // 根据 beanName 获取 bean 的类型
                    Class<?> beanType = this.beanFactory.getType(beanName, false);
                    if (beanType == null) {
                        continue;
                    } 
                    // 是切面【具有 @Aspect 注解并且不是由 ajc 编译】
                    if (this.advisorFactory.isAspect(beanType)) {
                        aspectNames.add(beanName);
                        AspectMetadata amd = new AspectMetadata(beanType, beanName);
                        // AspectJ支持的不同子句（切面实例化模型）是单例的
                        if (amd.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
                            MetadataAwareAspectInstanceFactory factory =
                                new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);
                            //获取 AspectJ 注解对应的切面增强处理类
                            List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
                            // 是单例则存入 Map<String, List<Advisor>> 缓存
                            if (this.beanFactory.isSingleton(beanName)) {
                                this.advisorsCache.put(beanName, classAdvisors);
                            }
                            else {
                                // 否则存入 Map<String, MetadataAwareAspectInstanceFactory> 缓存
                                this.aspectFactoryCache.put(beanName, factory);
                            }
                            advisors.addAll(classAdvisors);
                        }
                        else {
                            // Per target or per this.
                            if (this.beanFactory.isSingleton(beanName)) {
                                throw new IllegalArgumentException("Bean with name '" + beanName +
                                     "' is a singleton, but aspect instantiation model is not singleton");
                            }
                            MetadataAwareAspectInstanceFactory factory =
                                new PrototypeAspectInstanceFactory(this.beanFactory, beanName);
                            this.aspectFactoryCache.put(beanName, factory);
                            advisors.addAll(this.advisorFactory.getAdvisors(factory));
                        }
                    }
                }
                this.aspectBeanNames = aspectNames;
                return advisors;
            }
        }
    }
    // 没有获取到则返回空集合
    if (aspectNames.isEmpty()) {
        return Collections.emptyList();
    } 
    List<Advisor> advisors = new ArrayList<>();
    for (String aspectName : aspectNames) { 
        // 从缓存中获取
        List<Advisor> cachedAdvisors = this.advisorsCache.get(aspectName);
        if (cachedAdvisors != null) {
            advisors.addAll(cachedAdvisors);
        }
        else {
            MetadataAwareAspectInstanceFactory factory = this.aspectFactoryCache.get(aspectName);
            advisors.addAll(this.advisorFactory.getAdvisors(factory));
        }
    }
    return advisors;
}
```

```java
public List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory aspectInstanceFactory) {
    // 获取切面类  
    Class<?> aspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
    // 获取切面类名称
    String aspectName = aspectInstanceFactory.getAspectMetadata().getAspectName();
    // 校验
    validate(aspectClass);

    // We need to wrap the MetadataAwareAspectInstanceFactory with a decorator
    // so that it will only instantiate once. 用装饰器包装 MetadataAwareAspectInstanceFactory，使其仅实例化一次
    MetadataAwareAspectInstanceFactory lazySingletonAspectInstanceFactory =
        new LazySingletonAspectInstanceFactoryDecorator(aspectInstanceFactory);

    List<Advisor> advisors = new ArrayList<>();
    // 遍历所有的除标注 @Pointcut 注解的通知方法
    for (Method method : getAdvisorMethods(aspectClass)) {
        //TODO 获取对应的增强器
        Advisor advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, 0, aspectName);
        if (advisor != null) {
            advisors.add(advisor);
        }
    }

    // If it's a per target aspect, emit the dummy instantiating aspect.
    if (!advisors.isEmpty() && lazySingletonAspectInstanceFactory.getAspectMetadata().isLazilyInstantiated()) {
        Advisor instantiationAdvisor = new SyntheticInstantiationAdvisor(lazySingletonAspectInstanceFactory);
        advisors.add(0, instantiationAdvisor);
    }

    // Find introduction fields.
    for (Field field : aspectClass.getDeclaredFields()) {
        Advisor advisor = getDeclareParentsAdvisor(field);
        if (advisor != null) {
            advisors.add(advisor);
        }
    }

    return advisors;
}
```

######  获取切点表达式

```java
public Advisor getAdvisor(Method candidateAdviceMethod, MetadataAwareAspectInstanceFactory aspectInstanceFactory,
                          int declarationOrderInAspect, String aspectName) {

    validate(aspectInstanceFactory.getAspectMetadata().getAspectClass());
    // 获取切点表达式
    AspectJExpressionPointcut expressionPointcut = getPointcut(
        candidateAdviceMethod, aspectInstanceFactory.getAspectMetadata().getAspectClass());
    if (expressionPointcut == null) {
        return null;
    }
    //TODO  获取对应的增强器【重要】实例化模型切入点通知
    return new InstantiationModelAwarePointcutAdvisorImpl(expressionPointcut, candidateAdviceMethod,
                                                          this, aspectInstanceFactory, declarationOrderInAspect, aspectName);
}


```

```java
private AspectJExpressionPointcut getPointcut(Method candidateAdviceMethod, Class<?> candidateAspectClass) {
    // 获取方法上的 AspectJ 相关注解，包括 @Before，@After 等
    AspectJAnnotation<?> aspectJAnnotation =
            AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
    if (aspectJAnnotation == null) {
        return null;
    }

    // 创建一个 AspectJExpressionPointcut 对象
    AspectJExpressionPointcut ajexp =
            new AspectJExpressionPointcut(candidateAspectClass, new String[0], new Class<?>[0]);
    // 设置切点表达式
    ajexp.setExpression(aspectJAnnotation.getPointcutExpression());
    ajexp.setBeanFactory(this.beanFactory);
    return ajexp;
}

protected static AspectJAnnotation<?> findAspectJAnnotationOnMethod(Method method) {
    // classesToLookFor 中的元素是大家熟悉的
    Class<?>[] classesToLookFor = new Class<?>[] {
            Before.class, Around.class, After.class, AfterReturning.class, AfterThrowing.class, Pointcut.class};
    for (Class<?> c : classesToLookFor) {
        // 查找注解
        AspectJAnnotation<?> foundAnnotation = findAnnotation(method, (Class<Annotation>) c);
        if (foundAnnotation != null) {
            return foundAnnotation;
        }
    }
    return null;
}
```

@Before 注解中的表达式是pointcut()，也就是说 ajexp 设置的表达式只是一个中间值，不是最终值，即execution(* xyz.coolblog.aop.*.world*(..))。所以后续还需要将 ajexp 中的表达式进行转换
###### Advisor 实现类的创建过程

```java
//Advisor 实现类的创建过程。如下：
public InstantiationModelAwarePointcutAdvisorImpl(AspectJExpressionPointcut declaredPointcut,
                                                  Method aspectJAdviceMethod, AspectJAdvisorFactory aspectJAdvisorFactory,
                                                  MetadataAwareAspectInstanceFactory aspectInstanceFactory, int 		                                                           declarationOrder, String aspectName) {

    this.declaredPointcut = declaredPointcut;
    this.declaringClass = aspectJAdviceMethod.getDeclaringClass();
    this.methodName = aspectJAdviceMethod.getName();
    this.parameterTypes = aspectJAdviceMethod.getParameterTypes();
    this.aspectJAdviceMethod = aspectJAdviceMethod;
    this.aspectJAdvisorFactory = aspectJAdvisorFactory;
    this.aspectInstanceFactory = aspectInstanceFactory;
    this.declarationOrder = declarationOrder;
    this.aspectName = aspectName;

    if (aspectInstanceFactory.getAspectMetadata().isLazilyInstantiated()) {
        // Static part of the pointcut is a lazy type.
        Pointcut preInstantiationPointcut = Pointcuts.union(
            aspectInstanceFactory.getAspectMetadata().getPerClausePointcut(), this.declaredPointcut);

        // Make it dynamic: must mutate from pre-instantiation to post-instantiation state.
        // If it's not a dynamic pointcut, it may be optimized out
        // by the Spring AOP infrastructure after the first evaluation.
        this.pointcut = new PerTargetInstantiationModelPointcut(
            this.declaredPointcut, preInstantiationPointcut, aspectInstanceFactory);
        this.lazy = true;
    }
    else {
        // A singleton aspect.
        this.pointcut = this.declaredPointcut;
        this.lazy = false; 
        //TODO 实例化对相应的增强器
        this.instantiatedAdvice = instantiateAdvice(this.declaredPointcut);
    }
}

private Advice instantiateAdvice(AspectJExpressionPointcut pointcut) {
    // 由不同的切面注解获取不同的增强器
    Advice advice = this.aspectJAdvisorFactory.getAdvice(this.aspectJAdviceMethod, pointcut,
                    this.aspectInstanceFactory, this.declarationOrder, this.aspectName);
    return (advice != null ? advice : EMPTY_ADVICE);
}
```

```JAVA
public Advice getAdvice(Method candidateAdviceMethod, AspectJExpressionPointcut expressionPointcut,
                        MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrder, String aspectName) {
    // 获取切面类
    Class<?> candidateAspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
    validate(candidateAspectClass);
    // 获取切面方法上的通知类型
    AspectJAnnotation<?> aspectJAnnotation =
        AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
    if (aspectJAnnotation == null) {
        return null;
    }

    // If we get here, we know we have an AspectJ method.
    // Check that it's an AspectJ-annotated class
    // 由注解类型实例化不同的增强器
    if (!isAspect(candidateAspectClass)) {
        throw new AopConfigException("Advice must be declared inside an aspect type: " +
                                     "Offending method '" + candidateAdviceMethod + "' in class [" +
                                     candidateAspectClass.getName() + "]");
    }

    if (logger.isDebugEnabled()) {
        logger.debug("Found AspectJ method: " + candidateAdviceMethod);
    }

    AbstractAspectJAdvice springAdvice;
 	// 按照注解类型生成相应的 Advice 实现类
    switch (aspectJAnnotation.getAnnotationType()) {
        case AtPointcut:
            if (logger.isDebugEnabled()) {
                logger.debug("Processing pointcut '" + candidateAdviceMethod.getName() + "'");
            }
            return null;
        case AtAround:
            springAdvice = new AspectJAroundAdvice(
                candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
            break;
        case AtBefore:
            springAdvice = new AspectJMethodBeforeAdvice(
                candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
            break;
        case AtAfter:
            springAdvice = new AspectJAfterAdvice(
                candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
            break;
        case AtAfterReturning:
            springAdvice = new AspectJAfterReturningAdvice(
                candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
            AfterReturning afterReturningAnnotation = (AfterReturning) aspectJAnnotation.getAnnotation();
            if (StringUtils.hasText(afterReturningAnnotation.returning())) {
                springAdvice.setReturningName(afterReturningAnnotation.returning());
            }
            break;
        case AtAfterThrowing:
            springAdvice = new AspectJAfterThrowingAdvice(
                candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
            AfterThrowing afterThrowingAnnotation = (AfterThrowing) aspectJAnnotation.getAnnotation();
            if (StringUtils.hasText(afterThrowingAnnotation.throwing())) {
                springAdvice.setThrowingName(afterThrowingAnnotation.throwing());
            }
            break;
        default:
            throw new UnsupportedOperationException(
                "Unsupported advice type on method: " + candidateAdviceMethod);
    }

    // Now to configure the advice...
    // 配置属性
    springAdvice.setAspectName(aspectName);
    springAdvice.setDeclarationOrder(declarationOrder);
    /*
     * 获取方法的参数列表名称，比如方法 int sum(int numX, int numY), 
     * getParameterNames(sum) 得到 argNames = [numX, numY]
     */
    String[] argNames = this.parameterNameDiscoverer.getParameterNames(candidateAdviceMethod);
    if (argNames != null) {
        springAdvice.setArgumentNamesFromStringArray(argNames);
    }
    // 绑定参数
    springAdvice.calculateArgumentBindings();

    return springAdvice;
}
```

# 4、将目标对象变为代理对象

- 在IOC中对bean实例化完成会进行赋值操作，然后进行初始化。
- 再初始化前后 调用beanDefinition的方法 将其变为代理对象

```java
protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
    if (System.getSecurityManager() != null) {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            invokeAwareMethods(beanName, bean);
            return null;
        }, getAccessControlContext());
    }
    else {
        invokeAwareMethods(beanName, bean);
    }

    Object wrappedBean = bean;
    if (mbd == null || !mbd.isSynthetic()) {
        //在初始化前应用Bean后处理器 @PostConstruct 注解是在这里面进行处理的
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
    }

    try {
        //初始化init
        invokeInitMethods(beanName, wrappedBean, mbd);
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
            (mbd != null ? mbd.getResourceDescription() : null),
            beanName, "Invocation of init method failed", ex);
    }
    if (mbd == null || !mbd.isSynthetic()) {
        //初始化后应用Bean后处理器
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    return wrappedBean;
}
```

```java
public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
    if (bean != null) {
        Object cacheKey = getCacheKey(bean.getClass(), beanName);
        if (this.earlyProxyReferences.remove(cacheKey) != bean) {
            // 如果需要则进行包装
            return wrapIfNecessary(bean, beanName, cacheKey);
        }
    }
    return bean;
}
```

```java
protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
    if (StringUtils.hasLength(beanName) && this.targetSourcedBeans.contains(beanName)) {
        return bean;
    }
    // 没有代理过
    if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
        return bean;
    }
    if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;
    }

    // 根据bean查找合适的通知
    Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
    if (specificInterceptors != DO_NOT_PROXY) {
        this.advisedBeans.put(cacheKey, Boolean.TRUE);
        Object proxy = createProxy(
            bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
        this.proxyTypes.put(cacheKey, proxy.getClass());
        return proxy;
    }

    this.advisedBeans.put(cacheKey, Boolean.FALSE);
    return bean;
}
```

## - 筛选合适的通知器

**getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);**

```java
protected Object[] getAdvicesAndAdvisorsForBean(
    Class<?> beanClass, String beanName, @Nullable TargetSource targetSource) {
	//查找合适的通知器
    List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName);
    if (advisors.isEmpty()) {
        return DO_NOT_PROXY;
    }
    return advisors.toArray();
}
```

```java
protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
    // 查找所有的通知器
    List<Advisor> candidateAdvisors = findCandidateAdvisors();
    //筛选可应用在 beanClass 上的 Advisor，通过 ClassFilter 和 MethodMatcher
    //对目标类和方法进行匹配
    List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
    // 拓展筛选出通知器列表
    extendAdvisors(eligibleAdvisors);
    if (!eligibleAdvisors.isEmpty()) {
        eligibleAdvisors = sortAdvisors(eligibleAdvisors);
    }
    return eligibleAdvisors;
}
```

**findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);**

```java
protected List<Advisor> findAdvisorsThatCanApply(
    List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {

    ProxyCreationContext.setCurrentProxiedBeanName(beanName);
    try { //筛选
        return AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
    }
    finally {
        ProxyCreationContext.setCurrentProxiedBeanName(null);
    }
}
```

```java
public static List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> clazz) {
    if (candidateAdvisors.isEmpty()) {
        return candidateAdvisors;
    }
    List<Advisor> eligibleAdvisors = new ArrayList<>();
    for (Advisor candidate : candidateAdvisors) { 
        // 筛选 IntroductionAdvisor 类型的通知器
        if (candidate instanceof IntroductionAdvisor && canApply(candidate, clazz)) {
            eligibleAdvisors.add(candidate);
        }
    }
    boolean hasIntroductions = !eligibleAdvisors.isEmpty();
    for (Advisor candidate : candidateAdvisors) {
        if (candidate instanceof IntroductionAdvisor) {
            // already processed
            continue;
        }
        // 筛选普通类型的通知器
        if (canApply(candidate, clazz, hasIntroductions)) {
            eligibleAdvisors.add(candidate);
        }
    }
    return eligibleAdvisors;
}
```

```java
public static boolean canApply(Advisor advisor, Class<?> targetClass, boolean hasIntroductions) {
    if (advisor instanceof IntroductionAdvisor) {
        /*
         * 从通知器中获取类型过滤器 ClassFilter，并调用 matchers 方法进行匹配。
         * ClassFilter 接口的实现类 AspectJExpressionPointcut 为例，该类的
         * 匹配工作由 AspectJ 表达式解析器负责，具体匹配细节这个就没法分析了，我
         * AspectJ 表达式的工作流程不是很熟
         */
        return ((IntroductionAdvisor) advisor).getClassFilter().matches(targetClass);
    }
    else if (advisor instanceof PointcutAdvisor) {
        PointcutAdvisor pca = (PointcutAdvisor) advisor; 
        // 对于普通类型的通知器，这里继续调用重载方法进行筛选  hasIntroductions = false  targetClass == 目标类
        return canApply(pca.getPointcut(), targetClass, hasIntroductions);
    }
    else {
        return true;
    }
}
```

```java
public static boolean canApply(Pointcut pc, Class<?> targetClass, boolean hasIntroductions) {
    Assert.notNull(pc, "Pointcut must not be null");
    // 使用 ClassFilter 匹配 class 会进行@After("pointcut()") 匹配
    if (!pc.getClassFilter().matches(targetClass)) {
        return false;
    }

    MethodMatcher methodMatcher = pc.getMethodMatcher();
    if (methodMatcher == MethodMatcher.TRUE) {
        return true;
    }

    IntroductionAwareMethodMatcher introductionAwareMethodMatcher = null;
    if (methodMatcher instanceof IntroductionAwareMethodMatcher) {
        introductionAwareMethodMatcher = (IntroductionAwareMethodMatcher) methodMatcher;
    }

    /*
     * 查找当前类及其父类（以及父类的父类等等）所实现的接口，由于接口中的方法是 public，
     * 所以当前类可以继承其父类，和父类的父类中所有的接口方法
     */ 
    Set<Class<?>> classes = new LinkedHashSet<>();
    if (!Proxy.isProxyClass(targetClass)) {
        classes.add(ClassUtils.getUserClass(targetClass));
    }
    classes.addAll(ClassUtils.getAllInterfacesForClassAsSet(targetClass));

    for (Class<?> clazz : classes) {
        // 获取当前类的方法列表，包括从父类中继承的方法
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
        for (Method method : methods) {
            // 使用 methodMatcher 匹配方法，匹配成功即可立即返回
            if (introductionAwareMethodMatcher != null ?
                introductionAwareMethodMatcher.matches(method, targetClass, hasIntroductions) :
                methodMatcher.matches(method, targetClass)) {
                return true;
            }
        }
    }

    return false;
}

```

在 AOP 中，切点 Pointcut 是用来匹配连接点的，以 AspectJExpressionPointcut 类型的切点为例。该类型切点实现了ClassFilter 和 MethodMatcher 接口，匹配的工作则是由 AspectJ 表达式解析器复杂。除了使用 AspectJ 表达式进行匹配，Spring 还提供了基于正则表达式的切点类，以及更简单的根据方法名进行匹配的切点类。

## - 代理对象创建

```java
//匹配上进行代理
if (specificInterceptors != DO_NOT_PROXY) {
    this.advisedBeans.put(cacheKey, Boolean.TRUE);
    //代理
    Object proxy = createProxy(
        bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
    this.proxyTypes.put(cacheKey, proxy.getClass());
    return proxy;
}
```

```java
protected Object createProxy(Class<?> beanClass, @Nullable String beanName,
                             @Nullable Object[] specificInterceptors, TargetSource targetSource) {
// beanFactory【DefaultListableBeanFactory】 是 ConfigurableListableBeanFactory 的子类
    if (this.beanFactory instanceof ConfigurableListableBeanFactory) {
        //暴露目标对象 设置 originalTargetClass 属性值为 beanClass
        AutoProxyUtils.exposeTargetClass((ConfigurableListableBeanFactory) this.beanFactory, beanName, beanClass);
    }
	// 代理工厂
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.copyFrom(this);
	// proxyTargetClass 属性值是否为 False，默认为 False
    if (proxyFactory.isProxyTargetClass()) {
	  // 确定是否应使用给定的 Bean 替代其目标类而不是其接口 preserveTargetClass 属性值为 True        
        if (Proxy.isProxyClass(beanClass)) {
            // Must allow for introductions; can't just set interfaces to the proxy's interfaces only.
            for (Class<?> ifc : beanClass.getInterfaces()) {
                proxyFactory.addInterface(ifc);
            }
        }
    }
    else {
        // No proxyTargetClass flag enforced, let's apply our default checks...
        if (shouldProxyTargetClass(beanClass, beanName)) {
            proxyFactory.setProxyTargetClass(true);
        }
        else {
            evaluateProxyInterfaces(beanClass, proxyFactory);
        }
    }

    Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
    proxyFactory.addAdvisors(advisors);
    proxyFactory.setTargetSource(targetSource);
    customizeProxyFactory(proxyFactory);

    proxyFactory.setFrozen(this.freezeProxy);
    if (advisorsPreFiltered()) {
        proxyFactory.setPreFiltered(true);
    }
	// 获取代理对象【重要】
    return proxyFactory.getProxy(getProxyClassLoader());
}

public Object getProxy(@Nullable ClassLoader classLoader) {
    return createAopProxy().getProxy(classLoader);
}

protected final synchronized AopProxy createAopProxy() {
    if (!this.active) {
        activate();
    }
    return getAopProxyFactory().createAopProxy(this);
}
```

```java
public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
    // 需要优化【默认为False】 或者 proxyTargetClass属性值为True【默认为False】 或者 没有用户提供的代理接口
    if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
        // 目标类
        Class<?> targetClass = config.getTargetClass();
        if (targetClass == null) {
            throw new AopConfigException("TargetSource cannot determine target class: " +
                                         "Either an interface or a target is required for proxy creation.");
        }
        // 是接口 或者 是代理的类
        if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
            return new JdkDynamicAopProxy(config);
        }
        // 使用 JDK 动态代理
        return new ObjenesisCglibAopProxy(config);
    }
    else {
        // 默认使用 JDK 动态代理
        return new JdkDynamicAopProxy(config);
    }
}

public JdkDynamicAopProxy(AdvisedSupport config) throws AopConfigException {
    Assert.notNull(config, "AdvisedSupport must not be null");
    if (config.getAdvisors().length == 0 && config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE) {
        throw new AopConfigException("No advisors and no TargetSource specified");
    }
    this.advised = config;
}
//advised含有目标类与通知方法
```

# 5、调用代理类invoke

```java
class JdkDynamicAopProxy implements AopProxy, InvocationHandler, Serializable {
    
    invoke(){
        ``````
        // TODO 获取此方法的拦截链 Get the interception chain for this method.
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

        /**
		 * 检查是否有其他通知
		 * 如果没有，可以依靠目标直接反射调用，并避免创建 MethodInvocation
		 * Check whether we have any advice. If we don't, we can fallback on direct
         * reflective invocation of the target, and avoid creating a MethodInvocation.
		 **/
        if (chain.isEmpty()) {
            // We can skip creating a MethodInvocation: just invoke the target directly
            // Note that the final invoker must be an InvokerInterceptor so we know it does
            // nothing but a reflective operation on the target, and no hot swapping or fancy proxying.
            /**
			* 我们可以跳过创建 MethodInvocation 的操作：仅直接调用目标
			* 请注意，最终的调用者必须是 InvokerInterceptor 使给定参数适应给定方法中的目标签名
			**/
            Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
            // 通过反射调用给定目标
            retVal = AopUtils.invokeJoinpointUsingReflection(target, method, argsToUse);
        }
        else {
            // We need to create a method invocation...
            //创建一个 ReflectiveMethodInvocation
            MethodInvocation invocation =
                new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
            // Proceed to the joinpoint through the interceptor chain.
            // 通过拦截器链进入连接点【责任链模式】
            retVal = invocation.proceed();
        }
        ``````

    }
}
```

## - 获取要执行的目标方法拦截器链

- 遍历所有的增强器，将其转为Interceptor
- 将增强器转为List<MethodInterceptor>
  - 如果是MethodInterceptor类型直接加入
  - 如果不是，使用  AdvisorAdapter进行转化转化完成后加入到数组

```java
public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, @Nullable Class<?> targetClass) {
    MethodCacheKey cacheKey = new MethodCacheKey(method);
    List<Object> cached = this.methodCache.get(cacheKey);
    if (cached == null) {
        //使用通知链工厂进行获取拦截器
        cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(
            this, method, targetClass);
        this.methodCache.put(cacheKey, cached);
    }
    return cached;
}
```

this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this, method, targetClass);

```java
public List<Object> getInterceptorsAndDynamicInterceptionAdvice(
    Advised config, Method method, @Nullable Class<?> targetClass) {

    // 获取 DefaultAdvisorAdapterRegistry 实例
    AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();
    // 获取通知
    Advisor[] advisors = config.getAdvisors();
    //拦截器集合
    List<Object> interceptorList = new ArrayList<>(advisors.length);
    //目标类
    Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());
    Boolean hasIntroductions = null;

    for (Advisor advisor : advisors) {
        // 是 PointcutAdvisor 切入点通知类型，DefaultPointcutAdvisor 和 InstantiationModelAwarePointcutAdvisorImpl 都符合条件
        if (advisor instanceof PointcutAdvisor) {
            // Add it conditionally.
            PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
            // 代理配置以预先过【默认为False】 或者 切面与目标类匹配
            if (config.isPreFiltered() || pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
                // 获取 TrueMethodMatcher 或 AspectJExpressionPointcut 方法匹配器
                MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
                boolean match;
                // TrueMethodMatcher 不是 IntroductionAwareMethodMatcher 的子类，AspectJExpressionPointcut 是
                if (mm instanceof IntroductionAwareMethodMatcher) {
                    if (hasIntroductions == null) {
                        hasIntroductions = hasMatchingIntroductions(advisors, actualClass);
                    }
                    match = ((IntroductionAwareMethodMatcher) mm).matches(method, actualClass, hasIntroductions);
                }
                else {
                    match = mm.matches(method, actualClass);
                }
                // 匹配
                if (match) {
                    // 转换为拦截器
                    MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
                    if (mm.isRuntime()) {
                        // Creating a new object instance in the getInterceptors() method
                        // isn't a problem as we normally cache created chains.
                        for (MethodInterceptor interceptor : interceptors) {
                            // 封装为 InterceptorAndDynamicMethodMatcher
                            interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
                        }
                    }
                    else {
                        interceptorList.addAll(Arrays.asList(interceptors));
                    }
                }
            }
        }
        // 是 IntroductionAdvisor 类型
        else if (advisor instanceof IntroductionAdvisor) {
            IntroductionAdvisor ia = (IntroductionAdvisor) advisor;
            if (config.isPreFiltered() || ia.getClassFilter().matches(actualClass)) {
                Interceptor[] interceptors = registry.getInterceptors(advisor);
                interceptorList.addAll(Arrays.asList(interceptors));
            }
        }
        else {
            Interceptor[] interceptors = registry.getInterceptors(advisor);
            interceptorList.addAll(Arrays.asList(interceptors));
        }
    }

    return interceptorList;
}
```

 **registry.getInterceptors(advisor);**

```java
public MethodInterceptor[] getInterceptors(Advisor advisor) throws UnknownAdviceTypeException {
    List<MethodInterceptor> interceptors = new ArrayList<>(3);
    Advice advice = advisor.getAdvice();
    if (advice instanceof MethodInterceptor) {
        interceptors.add((MethodInterceptor) advice);
    }
    for (AdvisorAdapter adapter : this.adapters) {
        if (adapter.supportsAdvice(advice)) {
            interceptors.add(adapter.getInterceptor(advisor));
        }
    }
    if (interceptors.isEmpty()) {
        throw new UnknownAdviceTypeException(advisor.getAdvice());
    }
    return interceptors.toArray(new MethodInterceptor[0]);
}
//如 ：adapter.getInterceptor
public MethodInterceptor getInterceptor(Advisor advisor) {
    MethodBeforeAdvice advice = (MethodBeforeAdvice) advisor.getAdvice();
    return new MethodBeforeAdviceInterceptor(advice);
}
```

![image-20230625113423152](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/nova/spring-aop/2.png)

根据适配器将通知转为不同的通知拦截器

### -1-  Before

```java
class MethodBeforeAdviceAdapter implements AdvisorAdapter, Serializable {

    @Override
    public boolean supportsAdvice(Advice advice) {
        return (advice instanceof MethodBeforeAdvice);
    }

    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        MethodBeforeAdvice advice = (MethodBeforeAdvice) advisor.getAdvice();
        return new MethodBeforeAdviceInterceptor(advice);
    }

}
```

### -2-  After Around 

```java
转为 MethodInterceptor
```

### -3- Returning

```java
转为 AfterReturningAdviceInterceptor
```

![执行顺序](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/nova/spring-aop/3.png)





## 通知拦截器链从触发

- 从**-1**开始 进行++操作 取出数组的中的通知链
- 当索引与数组-1值一样时，（到了数组最后一个通知），执行目标方法
- 递归调用proceed（）

```java
public Object proceed() throws Throwable {
	/**
	 * 从索引 -1 开始并提前增加【通过递归调用】
	 * 确保所有的责任者都完成处理逻辑
	 **/
	// 是最后一个拦截器
	if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
		// 直接通过反射调用目标方法
		return invokeJoinpoint();
	}

	/**
	* 获取下一个责任者
	 *
	 * ExposeInvocationInterceptor
	 * AspectJAfterThrowingAdvice
	 * AfterReturningAdviceInterceptor
	 * AspectJAfterAdvice
	 * AspectJAroundAdvice
	 * MethodBeforeAdviceInterceptor
	 **/
	Object interceptorOrInterceptionAdvice =
			this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
	/**
	 * 是 InterceptorAndDynamicMethodMatcher 类型
	 * 这里都不是这个类型
	 **/
	if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
		
		InterceptorAndDynamicMethodMatcher dm =
				(InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
		Class<?> targetClass = (this.targetClass != null ? this.targetClass : this.method.getDeclaringClass());
		if (dm.methodMatcher.matches(this.method, targetClass, this.arguments)) {
			return dm.interceptor.invoke(this);
		}
		else {
			/**
			 * 动态匹配失败，跳过此拦截器并调用链中的下一个拦截器
			 **/
			return proceed();
		}
	}
	else {
		return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
	}
}

```

1）环绕AspectJAroundAdvice

```java

public Object invoke(MethodInvocation mi) throws Throwable {
    if (!(mi instanceof ProxyMethodInvocation)) {
        throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
    }
    ProxyMethodInvocation pmi = (ProxyMethodInvocation) mi;
    ProceedingJoinPoint pjp = lazyGetProceedingJoinPoint(pmi);
    JoinPointMatch jpm = getJoinPointMatch(pmi);
    // 调用环绕通知方法
    return invokeAdviceMethod(pjp, jpm, null, null);
}
protected Object invokeAdviceMethod(@Nullable JoinPointMatch jpMatch,
										@Nullable Object returnValue,
										@Nullable Throwable ex) throws Throwable {
	// 用给定的参数调用通知方法
	return invokeAdviceMethodWithGivenArgs(argBinding(getJoinPoint(), jpMatch, returnValue, ex));
}

protected Object invokeAdviceMethod(JoinPoint jp,
									@Nullable JoinPointMatch jpMatch,
									@Nullable Object returnValue,
									@Nullable Throwable t) throws Throwable {

	return invokeAdviceMethodWithGivenArgs(argBinding(jp, jpMatch, returnValue, t));
}

protected Object invokeAdviceMethodWithGivenArgs(Object[] args) throws Throwable {

	// 处理调用参数
	Object[] actualArgs = args;
	if (this.aspectJAdviceMethod.getParameterCount() == 0) {
		actualArgs = null;
	}

	try {
		// 使给定的方法可访问，并在必要时明确将其设置为可访问
		ReflectionUtils.makeAccessible(this.aspectJAdviceMethod);
		/**
		 * 反射调用增强处理方法
		 * @see AopUtils#invokeJoinpointUsingReflection(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
		 **/
		return this.aspectJAdviceMethod.invoke(this.aspectInstanceFactory.getAspectInstance(), actualArgs);
	}
	catch (IllegalArgumentException ex) {
		throw new AopInvocationException();
	}
	catch (InvocationTargetException ex) {
		throw ex.getTargetException();
	}
}

```

2）前置

```java
public Object invoke(MethodInvocation mi) throws Throwable {
    // 前置通知逻辑
    this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
    // 返回
    return mi.proceed();
}

```

3）后置

```java
public Object invoke(MethodInvocation mi) throws Throwable {
	try {
		// 返回
		return mi.proceed();
	}
	finally {
		// 返回通知一定会执行是因为放在 finally 块中
		invokeAdviceMethod(getJoinPointMatch(), null, null);
	}
}
```

4）返回通知

```java
public Object invoke(MethodInvocation mi) throws Throwable {
    Object retVal = mi.proceed();
    // 如果发生异常，则返回通知不执行，返回到异常通知逻辑，被捕获
    this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
    return retVal;
}
```

5）异常通知

```java
public Object invoke(MethodInvocation mi) throws Throwable {
	try {
		// 返回
		return mi.proceed();
	}
	catch (Throwable ex) {
		// 抛异常则调用
		if (shouldInvokeOnThrowing(ex)) {
			invokeAdviceMethod(getJoinPointMatch(), null, ex);
		}
		throw ex;
	}
}
```

6）默认通知

```java
public Object invoke(MethodInvocation mi) throws Throwable {
    MethodInvocation oldInvocation = invocation.get();
    invocation.set(mi);
    try {
        // 返回
        return mi.proceed();
    }
    finally {
        invocation.set(oldInvocation);
    }
}
```

![AnnotationAwareAspectJAutoProxyCreator](https://cdn.jsdelivr.net/gh/18500507445/drawing-bed/nova/spring-aop/4.jpg)










































