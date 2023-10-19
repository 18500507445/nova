## 秒杀、支付优化
### 简介:
* (1)主导重构了支付项目，但是没有自己的代码，所以我想把这块继续优化一下，细细打磨，从表的设计开始到代码的落地
* (2)同时呢也负责了一个藏品电商项目，下单流程可以进行优化
* (3)已接入api：微信开发者联盟sdk(支付,小程序,公众号)、支付宝、苹果支付、易宝支付(官方sdk)、谷歌支付(官方sdk)、快手支付、华为支付
* (4)[梳理的下单、支付流程图](https://www.processon.com/preview/642e300f769dd24760953fd7)

### 组织结构
```
nova-shopping
├── nova-shopping-common        --通用包
│     ├─annotation          --注解
│     ├─aop                 -- aop
│     ├─config              -- 配置类
│     ├─constant            -- 常量，拦截器，结果包装类
│     └─enums               -- 枚举类   
├── nova-shopping-order         --下单组件
└── nova-shopping-pay           --支付组件
      └─payment             -- wechat(微信支付工具、企业微信工具、商家微信工具、公众号)，open(其它各种支付工具)
```

### 秒杀超卖几种方案
#### 1.mysql 乐观锁（我操作的时候没人操作，如果有人操作我就再来一次）
~~~java
public String reduce(Integer id) {
    Goods goods = goodsMapper.selectById(id);
    if (goods != null && goods.getStock() > 0) {
        int i = goodsMapper.update(id, stock.getVersion());
        // i = 0 表示没有修改成功，说明有别人在你修改之前 已经修改了数据；需要重新再调用下当前方法
        if (i == 0) {
            // 防止栈溢出
            // Thread.sleep(10);
            this.reduce(id);
        }
    } else {
        throw new RuntimeException("库存不足！");
    }
}    
~~~

~~~sql
update goods set stock = stock - 1,version = version + 1 where id = #{id} and version = #{version}
~~~

#### 2.mysql 悲观锁（我操作的时候，不管有没有线程竞争，直接用上锁）
~~~java
@Transactional(rollbackFor = Exception.class)
public String reduce(Integer id) {
    // 加锁查询
    Goods goods = goodsMapper.selectForLock(id);
    if (goods != null && goods.getStock() > 0) {
        productStockMapper.update(id);
    } else {
        throw new RuntimeException("库存不足！");
    }
    return "ok";
}
~~~

~~~sql
select * from goods where id = {id} for update;

update goods set stock = stock - 1 where id = #{id} and stock > 0;
~~~

#### 3.使用job，在秒杀之前把库存预热，用redis的计数器，秒杀开始直接扣减redis，秒杀结束用job同步库存到mysql，扣减保持了原子性
优化版本：虽然扣减保持的原子性，但是扣减之前需要查询是否还有库存，这时我们可使用stock.lua脚本保证了查询和扣减两个操作原子性
~~~java
@Resource
private RedisScript<Long> redisScript;

Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("key"), Collections.EMPTY_LIST);
~~~

#### 4.redis分布式锁，lua 脚本 "return redis.call('set',KEYS[1], ARGV[1],'NX','PX',ARGV[2])"

#### 5.redisson分布式锁 本项目已经实现，直接在扣库存接口上加上注解

### 同一个用户有可能点击两次，导致多次下单
可以利用my_seckill_order表，userId+goodsId唯一索引，多次插入报错

### 项目启动参数
>nohup java -Xms1g -Xmx1g -Xmn256m -Xss512k -XX:PermSize=512m -XX:MaxPermSize=512m -jar /app/pay.jar >/dev/null 2>&1 &

### 排查配置微信支付问题，可以看到详细的返回错误信息（其它支付方式很少出问题）
~~~linux
cat sys-info.log |grep -A 16 'orderId'
~~~

### 收集的知识
* [淘宝面试：服务端防止重复支付](https://mp.weixin.qq.com/s/Xlo8yCPtjjG1SdF6DS8zpg)
* [30分钟未支付,自动取消,该怎么实现](https://mp.weixin.qq.com/s/fQ94NgKeR6qQAcIe0CrusA)
* [分布式限流 gateway限流，redis+lua实现限流，nginx限流](https://blog.csdn.net/qq_41979344/article/details/113105303)
* [分布式锁的实现与应用场景对比](https://blog.csdn.net/lemon89/article/details/52796775)