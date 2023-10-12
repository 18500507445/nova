### 面向对象设计六大原则
1. 里式替换原则
    - 多态，子类可扩展⽗类
2. 开闭原则
    - 抽象架构，扩展实现 （对修改关闭，对扩展开放） 继承父类扩展需要的方法，同时保留原有的方法，新增自己需要的方法
3. 依赖倒置原则
    - 实现开闭原则的重要途径之一，它降低了客户与实现模块之间的耦合
    - 细节依赖抽象，下层依赖上层
4. 迪⽶特法则原则
    - 最少知道，降低耦合
5. 接口隔离原则
    - 建⽴单⼀接⼝，尽量将庞大的接口拆分成更小&更具体的接口
6. 单一职责原则
    - ⼀个类和方法只做⼀件事
---

### 创建型
提供创建对象的机制， 能够提升已有代码的灵活性和可复用性。
1. [工厂方法（Factory Method）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/create/factory_method/工厂方法模式.md)
2. [抽象工厂（AbstractFactory）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/create/abstract_factory/抽象工厂模式.md)
3. [建造者（Builder）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/create/builder/建造者模式.md)
4. [原型（Prototype）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/create/prototype/原型模式.md)
5. [单例（Singleton）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/create/singleton/单例模式.md)

### 结构型
将对象和类按某种布局组成更大的结构，并同时保持结构的灵活和⾼效。
1. [适配器（Adapter）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/structure/adapter/适配器模式.md)
2. [桥接（Bridge）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/structure/bridge/桥接模式.md)
3. [组合（Composite）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/structure/composite/组合模式.md)
4. [装饰器模式（Decorator）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/structure/decorator/装饰器模式.md)
5. [外观（Facade）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/structure/facade/外观模式.md)
6. [享元（Flyweight）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/structure/flyweight/享元模式.md)
7. [代理（Proxy）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/structure/proxy/代理模式.md)

### 行为型
负责对象间的高效沟通和职责委派。
1. [职责链（Chain of Responsibility）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/chain/责任链模式.md)
2. [命令（Command）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/command/命令模式.md)
3. [解释器（Interpreter）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/interpreter/解释器模式.md)
4. [迭代器（Iterator）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/iterator/迭代器模式.md)
5. [中介者（Mediator）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/mediator/中介者模式.md)
6. [备忘录（Memento）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/memento/备忘录模式.md)
7. [观察者（Observer）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/observer/观察者模式.md)
8. [状态（State）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/state/状态模式.md)
9. [策略（Strategy）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/strategy/策略模式.md)
10. [模板方法（TemplateMethod）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/template/模板方法模式.md)
11. [访问者（Visitor）](/Users/wangzehui/Documents/IdeaProjects/nova/nova-book/src/main/java/com/nova/book/design/action/visitor/访问者模式.md)

##### 相关资料
- 23种设计模式-思维导图 https://www.processon.com/view/link/61dce52ef346fb06cb8f047a
- 23种设计模式-UML类图 https://www.processon.com/view/link/61dba3ad0e3e7441577314a5
- 图说设计模式 https://design-patterns.readthedocs.io/zh_CN/latest/index.html
- 菜鸟教程 https://www.runoob.com/design-pattern/design-pattern-tutorial.html
- 《重学Java设计模式》 https://github.com/fuzhengwei/itstack-demo-design
- 《设计模式之于我》 https://github.com/nackily/patterns-for-me
- 《25000 字详解23种设计模式（多图+代码）》https://mp.weixin.qq.com/s/LF8bzvggpsMcLN1_zIV8CQ
- 《设计模式系列笔记》 https://www.yuque.com/qingkongxiaguang/design/etrqih


