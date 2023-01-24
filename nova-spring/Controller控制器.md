### Controller控制器
有了SpringMVC之后，我们不必再像之前那样一个请求地址创建一个Servlet了，  
它使用`DispatcherServlet`替代Tomcat为我们提供的默认的静态资源Servlet，  
也就是说，现在所有的请求（除了jsp，因为Tomcat还提供了一个jsp的Servlet）都会经过`DispatcherServlet`进行处理

那么DispatcherServlet会帮助我们做什么呢？

![](/Users/wangzehui/IdeaProjects/nova/nova-spring/src/main/picture/DispatcherServlet流程.jpeg)