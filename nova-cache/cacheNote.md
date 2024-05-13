## nova-cache-limit
### 简介:
* (1)接口限流：单机版（ExtRateLimiter）、分布式版（AccessLimit）
* (2)请求和响应拦截器处理数据、validation、拦截异常统一封装
* (3)引入jedis、redisson实现布隆过滤器


## nova-cache-lock
### 简介:
* (1)锁的种类，redisson分布式锁，mysql悲观锁(for update)

### 知识点
* [《过滤器和拦截器的6个区别》](https://chengxy-nds.blog.csdn.net/article/details/106356958?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-2-106356958-blog-127438161.pc_relevant_recovery_v2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-2-106356958-blog-127438161.pc_relevant_recovery_v2&utm_relevant_index=3)
* [MySQL悲观锁](https://www.bilibili.com/video/BV1MY411R7bo/?vd_source=04ff874447812687f3346175b839011e)

