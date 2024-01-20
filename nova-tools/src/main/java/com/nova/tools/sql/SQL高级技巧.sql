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

## 有就更新（影响2行），无则插入（影响1行）
insert into study.user (id, name, age, email, user_name, `desc`, hide, status, gender, contact)
values (1, 'Jone', 18, 'test1@baomidou.com', null, null, null, 0, 1, null)
on duplicate key update age = age + 10;

