###消息队列
我们之前如果需要进行远程调用，那么一般可以通过发送HTTP请求来完成，而现在，我们可以使用第二种方式，  
就是消息队列，它能够将发送方发送的信息放入队列中，当新的消息入队时，会通知接收方进行处理，一般消息发送方称为生产者，接收方称为消费者。
![](https://www.yuque.com/api/filetransfer/images?url=https%3A%2F%2Ftva1.sinaimg.cn%2Flarge%2Fe6c9d24ely1h1aifat4bgj21qy0g6abi.jpg&sign=3b01a51d1189ea7a65f78a6e0485a7049397503eaced54f5eb68590ba700a15d)

这样我们所有的请求，都可以直接丢到消息队列中，再由消费者取出，不再是直接连接消费者的形式了，而是加了一个中间商，这也是一种很好的解耦方案，并且在高并发的情况下，由于消费者能力有限，消息队列也能起到一个削峰填谷的作用，堆积一部分的请求，再由消费者来慢慢处理，而不会像直接调用那样请求蜂拥而至。

RabbitMQ  -  性能很强，吞吐量很高，支持多种协议，集群化，消息的可靠执行特性等优势，很适合企业的开发。

[安装消息队列](https://wanghuichen.blog.csdn.net/article/details/124797746?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EOPENSEARCH%7ERate-5-124797746-blog-117995202.pc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EOPENSEARCH%7ERate-5-124797746-blog-117995202.pc_relevant_multi_platform_whitelistv3&utm_relevant_index=10)

进入了之后会显示当前的消息队列情况，包括版本号、Erlang版本等，这里需要介绍一下RabbitMQ的设计架构，这样我们就知道各个模块管理的是什么内容了：
![](https://www.yuque.com/api/filetransfer/images?url=https%3A%2F%2Ftva1.sinaimg.cn%2Flarge%2Fe6c9d24ely1h1bcul1hzzj21r40iogq3.jpg&sign=226777bfd1ff2ef32cf5aab861464e769d51cc6f3d295535739496c455ddd447)

* 生产者（Publisher）和消费者（Consumer）：不用多说了吧。
* Channel：我们的客户端连接都会使用一个Channel，再通过Channel去访问到RabbitMQ服务器，注意通信协议不是http，而是amqp协议。
* Exchange：类似于交换机一样的存在，会根据我们的请求，转发给相应的消息队列，每个队列都可以绑定到Exchange上，这样Exchange就可以将数据转发给队列了，可以存在很多个，不同的Exchange类型可以用于实现不同消息的模式。
* Queue：消息队列本体，生产者所有的消息都存放在消息队列中，等待消费者取出。
* Virtual Host：有点类似于环境隔离，不同环境都可以单独配置一个Virtual Host，每个Virtual Host可以包含很多个Exchange和Queue，每个Virtual Host相互之间不影响。

####使用消息队列
最简的的模型
![](https://www.yuque.com/api/filetransfer/images?url=https%3A%2F%2Ftva1.sinaimg.cn%2Flarge%2Fe6c9d24ely1h1cin640c8j21fg06ajrh.jpg&sign=a3549deb2c6c8c3a533e28bbcca61ba939a27385b6ab3c3bff35da4950ed689f)

（一个生产者 -> 消息队列 -> 一个消费者）

生产者只需要将数据丢进消息队列，而消费者只需要将数据从消息队列中取出，这样就实现了生产者和消费者的消息交互。我们现在来演示一下，首先进入到我们的管理页面，这里我们创建一个新的实验环境，只需要新建一个Virtual Host即可：
