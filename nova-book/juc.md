## Java并发编程
### 1. 进程与线程
#### 1.1 进程与线程
进程
- 程序由指令和数据组成，但这些指令要运行，数据要读写，就必须将指令加载至CPU，数据加载至内存。在指令运行过程中还需要用到磁盘、网络等设备。进程就是用来加载指令、管理内存、管理IO的
- 当一个程序被运行，从磁盘加载这个程序的代码至内存，这时就开启了一个进程。
- 进程就可以视为程序的一个实例。大部分程序可以同时运行多个实例进程（例如记事本、画图、浏览器等），也有的程序只能启动一个实例进程（例如网易云音乐、360安全卫士等）

线程
- 一个进程之内可以分为一到多个线程。
- 一个线程就是一个指令流，将指令流中的一条条指令以一定的顺序交给CPU执行
- Java中，线程作为最小调度单位，进程作为资源分配的最小单位。 在windows中进程是不活动的，只是作为线程的容器

二者对比
- 进程基本上相互独立的，而线程存在于进程内，是进程的一个子集
- 进程拥有共享的资源，如内存空间等，供其内部的线程共享
- 进程间通信较为复杂，（1）同一台计算机的进程通信称为 IPC（Inter-process communication），（2）不同计算机之间的进程通信，需要通过网络，并遵守共同的协议，例如 HTTP
- 线程通信相对简单，因为它们共享进程内的内存，一个例子是多个线程可以访问同一个共享变量
- 线程更轻量，线程上下文切换成本一般上要比进程上下文切换低

#### 1.2 并行与并发
> 单核cpu下，线程实际还是串行执行的。操作系统中有一个组件叫做任务调度器，将cpu的时间片（windows下时间片最小约为15毫秒）分给不同的程序使用，  
只是由于cpu在线程间（时间片很短）的切换非常快，人类感觉是同时运行的。  
总结为一句话就是：微观串行，宏观并行，一般会将这种线程轮流使用CPU的做法称为并发，concurrent

> 多核cpu下，每个核（core）都可以调度运行线程，这时候线程可以是并行的。

引用RobPike（golang语言创造者）的一段描述：
- 并发（concurrent）是同一时间应对（dealing with）多件事情的能力
- 并行（parallel）是同一时间动手做（doing）多件事情的能力

例子
- 家庭主妇做饭、打扫卫生、给孩子喂奶，她一个人轮流交替做这多件事，这时就是并发
- 家庭主妇雇了个保姆，她们一起这些事，这时既有并发，也有并行（这时会产生竞争，例如锅只有一口，一个人用锅时，另一个人就得等待）
- 雇了3个保姆，一个专做饭、一个专打扫卫生、一个专喂奶，互不干扰，这时是并行

#### 1.3 应用
> 多线程可以让方法执行变为异步的（即不要巴巴干等着）比如说读取磁盘文件时，假设读取操作花费了5秒钟，
> 如果没有线程调度机制，这5秒cpu什么都做不了，其它代码都得暂停...

应用之异步调用
- 需要等待结果返回，才能继续运行就是同步
- 不需要等待结果返回，就能继续运行就是异步


### 2. Java线程
#### 2.1 创建和运行线程
原理之Thread与Runnable的关系
- 方法demoA是把线程和任务合并在了一起，方法demoB是把线程和任务分开了
- 用Runnable更容易与线程池等高级API配合
- 用Runnable让任务类脱离了Thread继承体系，更灵活

#### 2.2 观察多个线程同时运行
见section2例子

#### 2.3 查看进程方法
linux
- ps -fe 查看所有进程
- ps -fT -p <PID> 查看某个进程（PID）的所有线程
- kill 杀死进程
- top 按大写 H 切换是否显示线程
- top -H -p <PID> 查看某个进程（PID）的所有线程

#### 2.4线程运行原理
![线程运行原理](https://img-blog.csdnimg.cn/784158a11c924f4c9e376e12a37ebe6b.png#pic_center)
栈与栈帧
> Java Virtual Machine Stacks （Java虚拟机栈）
我们都知道JVM中由堆、栈、方法区所组成，其中栈内存是给谁用的呢？其实就是线程，每个线程启动后，虚拟 机就会为其分配一块栈内存
> - 每个栈由多个栈帧（Frame）组成，对应着每次方法调用时所占用的内存
> - 每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法

线程上下文切换（Thread Context Switch）
> 因为以下一些原因导致cpu不再执行当前的线程，转而执行另一个线程的代码
> - 线程的cpu时间片用完
> - 垃圾回收
> - 有更高优先级的线程需要运行
> - 线程自己调用了sleep、yield、wait、join、park、synchronized、lock等方法

> 当ContextSwitch发生时，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，Java中对应的概念
就是程序计数器（ProgramCounterRegister），它的作用是记住下一条jvm指令的执行地址，是线程私有的
> - 状态包括程序计数器、虚拟机栈中每个栈帧的信息，如局部变量、操作数栈、返回地址等
> - Context Switch频繁发生会影响性能

#### 2.5常见方法
- run：直接调用run是在主线程中执行了run，没有启动新的线程
- start：使用start是启动新的线程，通过新的线程间接执行run中的代码

> sleep
> - 调用sleep会让当前线程从Running进入TimedWaiting状态（阻塞）
> - 其它线程可以使用interrupt方法打断正在睡眠的线程，这时sleep方法会抛出InterruptedException
> - 睡眠结束后的线程未必会立刻得到执行
> - 建议用TimeUnit的sleep代替Thread的sleep来获得更好的可读性

> yield
> - 调用yield会让当前线程从Running进入Runnable就绪状态，然后调度执行其它线程
> - 具体的实现依赖于操作系统的任务调度器

> 线程优先级
> - 线程优先级会提示（hint）调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它
> - 如果cpu比较忙，那么优先级高的线程会获得更多的时间片，但cpu闲时，优先级几乎没作用

主线程与守护线程
> 默认情况下，Java 进程需要等待所有线程都运行结束，才会结束。有一种特殊的线程叫做守护线程，只要其它非守
护线程运行结束了，即使守护线程的代码没有执行完，也会强制结束。

#### 2.6五种状态
![6五种状态](https://img-blog.csdnimg.cn/10e02179a4bb4030972df3a4e4b2576d.png#pic_center)
- 【初始状态】仅是在语言层面创建了线程对象，还未与操作系统线程关联
- 【可运行状态】（就绪状态）指该线程已经被创建（与操作系统线程关联），可以由CPU调度执行
- 【运行状态】指获取了CPU时间片运行中的状态 （当CPU时间片用完，会从【运行状态】转换至【可运行状态】，会导致线程的上下文切换）
- 【阻塞状态】
  > 如果调用了阻塞API，如BIO读写文件，这时该线程实际不会用到CPU，会导致线程上下文切换，进入【阻塞状态】  
  > 等BIO操作完毕，会由操作系统唤醒阻塞的线程，转换至【可运行状态】  
  > 与【可运行状态】的区别是，对【阻塞状态】的线程来说只要它们一直不唤醒，调度器就一直不会考虑调度它们  
- 【终止状态】表示线程已经执行完毕，生命周期已经结束，不会再转换为其它状态

### 3. 共享模型之管程
#### 3.1 共享问题
临界区Critical Section  
一个程序运行多个线程本身是没有问题的，问题出在多个线程访问共享资源
- 多个线程读共享资源其实也没有问题
- 在多个线程对共享资源读写操作时发生指令交错，就会出现问题

一段代码块内如果存在对共享资源的多线程读写操作，称这段代码块为临界区
~~~java
static int counter = 0;
static void increment() {
// 临界区
 counter++; 
}
static void decrement() {
// 临界区
 counter--;
}
~~~
#### 3.2 synchronized
为了避免临界区的竞态条件发生，有多种手段可以达到目的。
- 阻塞式的解决方案：synchronized，Lock
- 非阻塞式的解决方案：原子变量

#### 3.3 线程安全分析
成员变量和静态变量是否线程安全？
- 如果它们没有共享，则线程安全
- 如果它们被共享了，根据它们的状态是否能够改变，又分两种情况
> 如果只有读操作，则线程安全  
如果有读写操作，则这段代码是临界区，需要考虑线程安全

局部变量是否线程安全？
- 局部变量是线程安全的
- 但局部变量引用的对象则未必
> 如果该对象没有逃离方法的作用访问，它是线程安全的  
如果该对象逃离方法的作用范围（return object），需要考虑线程安全

常见的线程安全类：String、Integer、StringBuffer、Random、Vector、Hashtable、juc包下的类

例子：ExerciseSell（卖票问题）、ExerciseTransfer（转账问题）

#### 3.4 Monitor（监视器、锁）
synchronized原理进阶
- 轻量级锁：如果一个对象虽然有多线程访问，但多线程访问的时间是错开的（没有竞争），那么就可以用轻量级锁来优化
~~~java
static final Object obj = new Object();

public static void m1(){
    synchronized(obj){
        //同步块a
        m2();
    }
}

public static void m2(){
    synchronized(obj){
        //同步块b
  }
}
~~~

- 自旋锁就是线程重试加锁（多核cpu下才有意义，jvm自动调整自旋次数）

#### 3.5 wait / notify
- obj.wait()让进入object监视器的线程到waitSet等待
- obj.notify()在object上正在waitSet等待的线程中挑一个唤醒
- obj.notifyAll()让object上正在waitSet等待的线程全部唤醒

sleep(long n)和wait(long n)的区别
- sleep是Thread方法，而wait是Object的方法
- sleep不需要强制和synchronized配合使用，但wait需要和synchronized一起用
- sleep在睡眠的同时，不会释放对象锁的，但wait在等待的时候会释放对象锁
- 它们状态TIMED_WAITING

wait notify的正确姿势（CorrectPostureStep）
~~~java
synchronized(lock) {
    while(条件不成立) {
        lock.wait();
 }
 // 干活
}

//另一个线程
synchronized(lock) {
    lock.notifyAll();
}
~~~

设计模式之保护性暂停（Guarded例子）

异步模式之生产者、消费者


#### 3.5 重新理解线程状态转换
![线程状态转换](https://img-blog.csdnimg.cn/471319664de74681bf06c147ca967941.png#pic_center)
- 情况1 NEW-->RUNNABLE
> 当调用t.start()方法时，由NEW-->RUNNABLE

- 情况2 RUNNABLE<-->WAITING
> t线程用 synchronized(obj) 获取了对象锁后  
调用obj.wait()方法时，t线程从RUNNABLE-->WAITING  
调用obj.notify()，obj.notifyAll()，t.interrupt()时  
竞争锁成功，t线程从WAITING-->RUNNABLE  
竞争锁失败，t线程从WAITING-->BLOCKED

- 情况3 RUNNABLE <--> WAITING
> 当前线程调用 t.join() 方法时，当前线程从 RUNNABLE --> WAITING  
注意是当前线程在t线程对象的监视器上等待  
t线程运行结束，或调用了当前线程的interrupt()时，当前线程从WAITING-->RUNNABLE  

- 情况4 RUNNABLE<-->WAITING
> 当前线程调用LockSupport.park()方法会让当前线程从RUNNABLE-->WAITING  
调用LockSupport.unpark(目标线程)或调用了线程的interrupt()，会让目标线程从WAITING-->RUNNABLE  

- 情况5 RUNNABLE<-->TIMED_WAITING
> t线程用synchronized(obj)获取了对象锁后  
调用obj.wait(long n)方法时，t线程从RUNNABLE-->TIMED_WAITING  
t线程等待时间超过了n毫秒，或调用obj.notify()，obj.notifyAll()，t.interrupt()时  
竞争锁成功，t线程从TIMED_WAITING-->RUNNABLE  
竞争锁失败，t线程从TIMED_WAITING-->BLOCKED  

- 情况6 RUNNABLE<-->TIMED_WAITING
> 当前线程调用t.join(long n)方法时，当前线程从RUNNABLE-->TIMED_WAITING  
注意是当前线程在t线程对象的监视器上等待  
当前线程等待时间超过了n毫秒，或t线程运行结束，或调用了当前线程的interrupt()时，当前线程从TIMED_WAITING-->RUNNABLE  

- 情况7 RUNNABLE<-->TIMED_WAITING
> 当前线程调用Thread.sleep(long n)，当前线程从RUNNABLE-->TIMED_WAITING    
当前线程等待时间超过了n毫秒，当前线程从TIMED_WAITING-->RUNNABLE  

- 情况8 RUNNABLE<-->TIMED_WAITING
> 当前线程调用LockSupport.parkNanos(long nanos)或LockSupport.parkUntil(long millis)时，当前线程从RUNNABLE-->TIMED_WAITING  
调用LockSupport.unpark(目标线程)或调用了线程的interrupt()，或是等待超时，会让目标线程从TIMED_WAITING-->RUNNABLE  

- 情况9 RUNNABLE<-->BLOCKED
> t线程用synchronized(obj)获取了对象锁时如果竞争失败，从RUNNABLE-->BLOCKED  
持obj锁线程的同步代码块执行完毕，会唤醒该对象上所有BLOCKED的线程重新竞争，如果其中t线程竞争成功，从BLOCKED-->RUNNABLE，其它失败的线程仍然BLOCKED  

- 情况10 RUNNABLE<-->TERMINATED
> 当前线程所有代码运行完毕，进入TERMINATED  

#### 3.6 多把锁
- 将锁的粒度细分
> 好处，是可以增强并发度  
坏处，如果一个线程需要同时获得多把锁，就容易发生死锁

#### 3.7 可重入锁ReentrantLock
> 可重入是指同一个线程如果首次获得了这把锁，那么因为它是这把锁的拥有者，因此有权利再次获取这把锁,如果是不可重入锁，那么第二次获得锁时，自己也会被锁挡住

相对于synchronized都支持可重入，还具备以下特点
- 可中断
- 可设置超时时间
- 可设置为公平锁
- 支持多个条件变量

~~~java
// 获取锁
reentrantLock.lock();
try {
    // 临界区
} finally {
    // 释放锁
    reentrantLock.unlock();
}
~~~


### 4. 共享模型之内存
#### 4.1 Java内存模型
JMM即JavaMemoryModel，它定义了主存、工作内存抽象概念，底层对应着CPU寄存器、缓存、硬件内存、CPU指令优化等
> JMM体现在以下几个方面  
原子性 - 保证指令不会受到线程上下文切换的影响  
可见性 - 保证指令不会受cpu缓存的影响  
有序性 - 保证指令不会受cpu指令并行优化的影响

指令重排：参照Jvm.ConcurrencyTest

单例模式学习：参照Design.singleton

### 5. 共享模型之无锁
#### 5.1 CAS与volatile
CAS的特点

结合CAS和volatile可以实现无锁并发，适用于线程数少、多核CPU的场景下。  
>（1）CAS是基于乐观锁的思想：最乐观的估计，不怕别的线程来修改共享变量，就算改了也没关系，我吃亏点再重试呗。  
（2）synchronized是基于悲观锁的思想：最悲观的估计，得防着其它线程来修改共享变量，我上了锁你们都别想改，我改完了解开锁，你们才有机会。  
（3）CAS体现的是无锁并发、无阻塞并发，请仔细体会这两句话的意思
> - 因为没有使用synchronized，所以线程不会陷入阻塞，这是效率提升的因素之一
> - 但如果竞争激烈，可以想到重试必然频繁发生，反而效率会受影响

### 6. 共享模型之不可变
#### 6.1 日期转换问题
例子-DateSafe

#### 6.2 不可变设计
基本数据类型包装类、String串池、BigDecimal、例子（数据库连接池）

### 7. 并发工具
#### 7.1 自定义线程池
![自定义线程池](https://img-blog.csdnimg.cn/d91246cd84994c49b50096c66a327dce.png#pic_center)

#### 7.2 ThreadPoolExecutor
构造方法例子：ManualJob

线程池工厂方法：chapter9.SectionTest3

提交任务
~~~java
// 执行任务
void execute(Runnable command);

// 提交任务 task，用返回值 Future 获得任务执行结果
<T> Future<T> submit(Callable<T> task);

// 提交 tasks 中所有任务
<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException;

// 提交 tasks 中所有任务，带超时时间
<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,long timeout, TimeUnit unit) throws InterruptedException;

// 提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消
<T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException;

// 提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消，带超时时间
<T> T invokeAny(Collection<? extends Callable<T>> tasks,long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
~~~

关闭线程池
~~~java
/*
 * 线程池状态变为 SHUTDOWN，不会接收新任务，但已提交任务会执行完，此方法不会阻塞调用线程的执行
 */
void shutdown();

/*
 * 线程池状态变为 STOP，不会接收新任务，会将队列中的任务返回，并用interrupt的方式中断正在执行的任务
 */
List<Runnable> shutdownNow();
~~~

线程池创建多少线程合适
> CPU密集型运算：数据分析  
I/O密集型运算：I/O操作、远程PRC调用、数据库操作。公式--> 线程数 = 核数 * 期望cpu利用率 * 总时间（CPU计算时间+等待时间）/ CPU计算时间  
例如：4核CPU计算时间是10%，其它等待时间是90%，期望cpu被100%利用，套用公式
4* 100% * 100% / 10% = 40

#### 7.3 AQS原理、ReentrantLock原理
全称是AbstractQueuedSynchronizer，是阻塞式锁和相关的同步器工具的框架
> 用state属性来表示资源的状态（分独占模式和共享模式），子类需要定义如何维护这个状态，控制如何获取锁和释放锁

#### 7.4 读写锁
ReentrantReadWriteLock

Jdk8进一步优化，加入StampedLock

#### 7.5 Semaphore-信号量
像一个停车场，permits就好像停车位数量，当线程获得了permits 就像是获得了停车位，然后停车场显示空余车位减一