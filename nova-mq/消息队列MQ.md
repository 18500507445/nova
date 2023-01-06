###消息队列
我们之前如果需要进行远程调用，那么一般可以通过发送HTTP请求来完成，而现在，我们可以使用第二种方式，  
就是消息队列，它能够将发送方发送的信息放入队列中，当新的消息入队时，会通知接收方进行处理，一般消息发送方称为生产者，接收方称为消费者。
![](https://www.yuque.com/api/filetransfer/images?url=https%3A%2F%2Ftva1.sinaimg.cn%2Flarge%2Fe6c9d24ely1h1aifat4bgj21qy0g6abi.jpg&sign=3b01a51d1189ea7a65f78a6e0485a7049397503eaced54f5eb68590ba700a15d)

这样我们所有的请求，都可以直接丢到消息队列中，再由消费者取出，不再是直接连接消费者的形式了，而是加了一个中间商，这也是一种很好的解耦方案，并且在高并发的情况下，由于消费者能力有限，消息队列也能起到一个削峰填谷的作用，堆积一部分的请求，再由消费者来慢慢处理，而不会像直接调用那样请求蜂拥而至。

RabbitMQ ：性能很强，吞吐量很高，支持多种协议，集群化，消息的可靠执行特性等优势，很适合企业的开发。

[安装消息队列](https://wanghuichen.blog.csdn.net/article/details/124797746?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EOPENSEARCH%7ERate-5-124797746-blog-117995202.pc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EOPENSEARCH%7ERate-5-124797746-blog-117995202.pc_relevant_multi_platform_whitelistv3&utm_relevant_index=10)

进入了之后会显示当前的消息队列情况，包括版本号、Erlang版本等，这里需要介绍一下RabbitMQ的设计架构，这样我们就知道各个模块管理的是什么内容了：
![](https://img-blog.csdnimg.cn/304b9d25cb744fefa4c097ef19be5b7a.png#pic_center)

* Server：又称Broker ,接受客户端的连接，实现AMQP实体服务。 安装rabbitmq-server
* Connection：连接，应用程序与Broker的网络连接 TCP/IP/ 三次握手和四次挥手
* Channel：网络信道，几乎所有的操作都在Channel中进行，Channel是进行消息读写的通道，客户端可以建立对各Channel，每个Channel代表一个会话任务。
* Message :消息：服务与应用程序之间传送的数据，由Properties和body组成，Properties可是对消息进行修饰，比如消息的优先级，延迟等高级特性，Body则就是消息体的内容。
* Virtual Host 虚拟地址，用于进行逻辑隔离，最上层的消息路由，一个虚拟主机理由可以有若干个Exhange和Queueu，同一个虚拟主机里面不能有相同名字的Exchange
* Exchange：交换机，接受消息，根据路由键发送消息到绑定的队列。(==不具备消息存储的能力==)
* Bindings：Exchange和Queue之间的虚拟连接，binding中可以保护多个routing key.
* Routing key：是一个路由规则，虚拟机可以用它来确定如何路由一个特定消息。
* Queue：队列：也成为Message Queue,消息队列，保存消息并将它们转发给消费者。


####使用消息队列
最简的的模型
![](https://www.yuque.com/api/filetransfer/images?url=https%3A%2F%2Ftva1.sinaimg.cn%2Flarge%2Fe6c9d24ely1h1cin640c8j21fg06ajrh.jpg&sign=a3549deb2c6c8c3a533e28bbcca61ba939a27385b6ab3c3bff35da4950ed689f)

（一个生产者 -> 消息队列 -> 一个消费者）

生产者只需要将数据丢进消息队列，而消费者只需要将数据从消息队列中取出，这样就实现了生产者和消费者的消息交互。我们现在来演示一下，首先进入到我们的管理页面，这里我们创建一个新的实验环境，只需要新建一个Virtual Host即可：
