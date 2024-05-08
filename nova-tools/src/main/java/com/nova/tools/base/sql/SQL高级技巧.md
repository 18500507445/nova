
~~~sql
## 自定义排序（order by field），按照id=1，2，3进行排序
select * from study.user order by field(id,1,2,3);

## 排序空值，如果是null转为0，否则为1
select * from study.user order by if(isnull(id),0,1);

## case when表达式
select *
from study.user
order by case when id = 1 then 1 when id = 2 then 2 when id = 3 then 3 else 4 end;

## 分组连接函数group_concat，支持排序和拼接
select age,group_concat(id order by id desc separator '_') as name from study.user group by age;

## 分组统计后再汇总求和，with rollup（最后一行就是再求和的数据）
select age,count(1),sum(age) as age_sum from study.user group by age with rollup;

## 子查询提取
with m1 as(select * from study.user where age > 20) select * from m1 where m1.id > 1;

## 优雅插入ignore，有就忽略，无则插入（按照唯一索引来插入的）
insert ignore into study.user (id, name, age, email, user_name, `desc`, hide, status, gender, contact)
values (1, 'Jone', 18, 'test1@baomidou.com', null, null, null, 0, 1, null);

## 有则删除+插入，无则插入
replace into study.user (id, name, age, email, user_name, `desc`, hide, status, gender, contact)
values (1, 'Jone', 18, 'test1@baomidou.com', null, null, null, 0, 1, null);

## 有就更新（影响2行），无则插入（影响1行），受唯一索引影响
insert into study.user (id, name, age, email, user_name, `desc`, hide, status, gender, contact)
values (1, 'Jone', 18, 'test1@baomidou.com', null, null, null, 0, 1, null)
on duplicate key update age = age + 10;
~~~

### 单独插入更新（根据唯一索引修改）
~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.avic.enterprise.custom.repository.mapper.EnterpriseCustomPoolMerchandiseSaleLimitMapper">

    <insert id="insertOrUpdate" keyColumn="id" keyProperty="id" parameterType="com.avic.enterprise.custom.repository.entity.EnterpriseCustomPoolMerchandiseSaleLimit" useGeneratedKeys="true">
        insert into enterprise_custom_pool_merchandise_sale_limit
        <trim prefix="(" suffix=")" suffixOverrides=",">
            pool_id,
            `key`,
            limit_price,
        </trim>
        values
        <trim prefix="(" suffix=")" suffixOverrides=",">
            #{poolId,jdbcType=BIGINT},
            #{key,jdbcType=VARCHAR},
            #{limitPrice,jdbcType=BIGINT},
        </trim>
        on duplicate key update
        <trim suffixOverrides=",">
            limit_price = #{limitPrice,jdbcType=BIGINT},
        </trim>
    </insert>
    
</mapper>
~~~

### 批量插入更新（根据唯一索引修改）
~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.avic.enterprise.custom.repository.mapper.EnterpriseCustomPoolMerchandiseSaleLimitMapper">

    <insert id="batchInsert" keyColumn="id" keyProperty="id" parameterType="map" useGeneratedKeys="true">
        insert into enterprise_custom_pool_merchandise_sale_limit
            (pool_id, `key`, limit_price)
        values
        <foreach collection="list" item="item" separator=",">
            (#{item.poolId,jdbcType=BIGINT}, #{item.key,jdbcType=VARCHAR}, #{item.limitPrice,jdbcType=BIGINT})
        </foreach>
        on duplicate key update
        <trim suffixOverrides=",">
            limit_price = values(limit_price)
        </trim>
    </insert>
</mapper>
~~~


### mybatis if标签判断boolean
~~~xml
<if test="flag != null and 'true'.toString() == flag.toString()">
    flag = #{flag, jdbcType=BOOLEAN}
</if>
~~~