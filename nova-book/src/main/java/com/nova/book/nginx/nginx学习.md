## Java虚拟机

正向代理
![正向代理](./img/正向代理.png)

反向代理
![反向代理](./img/反向代理.png)

#### Nginx的优点

##### (1)速度更快、并发更高

单次请求或者高并发请求的环境下，Nginx都会比其他Web服务器响应的速度更快。一方面在正常情况下，单次请求会得到更快的响应，另一方面，在高峰期(
如有数以万计的并发请求)
，Nginx比其他Web服务器更快的响应请求。Nginx之所以有这么高的并发处理能力和这么好的性能原因在于Nginx采用了多进程和I/O多路复用(
epoll)的底层实现。

##### (2)配置简单，扩展性强

Nginx的设计极具扩展性，它本身就是由很多模块组成，这些模块的使用可以通过配置文件的配置来添加。这些模块有官方提供的也有第三方提供的模块，如果需要完全可以开发服务自己业务特性的定制模块。

##### (3)高可靠性

Nginx采用的是多进程模式运行，其中有一个master主进程和N多个worker进程，worker进程的数量我们可以手动设置，每个worker进程之间都是相互独立提供服务，并且master主进程可以在某一个worker进程出错时，快速去"
拉起"新的worker进程提供服务。

##### (4)热部署

现在互联网项目都要求以7*24小时进行服务的提供，针对于这一要求，Nginx也提供了热部署功能，即可以在Nginx不停止的情况下，对Nginx进行文件升级、更新配置和更换日志文件等功能。

##### (5)成本低、BSD许可证

BSD是一个开源的许可证，世界上的开源许可证有很多，现在比较流行的有六种分别是GPL、BSD、MIT、Mozilla、Apache、LGPL。这六种的区别是什么，我们可以通过下面一张图来解释下：

#### Nginx5中分发方式

- 1、轮询（默认）

> 每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器down掉，能自动剔除

- 2、weight

> 指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况。

~~~
upstream bakend {
server 192.168.0.14 weight=10;
server 192.168.0.15 weight=10;
}
~~~

- 3、ip_hash

> 每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题。

~~~
upstream bakend {
ip_hash;
server 192.168.0.14:88;
server 192.168.0.15:80;
}
~~~

- 4、fair（第三方）

> 按后端服务器的响应时间来分配请求，响应时间短的优先分配。

~~~
upstream backend {
server server1;
server server2;
fair;
}
~~~

- 5、url_hash（第三方）

> 按访问url的hash结果来分配请求，使每个url定向到同一个后端服务器，后端服务器为缓存时比较有效。  
> 例：在upstream中加入hash语句，server语句中不能写入weight等其他的参数，hash_method是使用的hash算法

~~~
upstream backend {
server squid1:3128;
server squid2:3128;
hash $request_uri;
hash_method crc32;
}
~~~



