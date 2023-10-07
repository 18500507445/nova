--传入令牌桶得key和时间戳得key
local token_key=KEYS[1]
local time_key=KEYS[2]

-- 分别传入 速率，容量，现在得时间，请求得令牌数
local rate=tonumber(ARGV[1])
local capacity=tonumber(ARGV[2])
local requestNum=tonumber(ARGV[3])
local now_time=redis.call('TIME')[1]
--获取上次访问 得令牌树和时间绰
local last_tokens=tonumber(redis.call('get',token_key))
if last_tokens==nil then
    last_tokens=capacity
end
local last_time=tonumber(redis.call('get',time_key))
if last_time==nil then
    last_time=0
end
--1，计算时间差
local time_del=math.max(0,(now_time-last_time))
--2,计算当前令牌桶中应该有多少令牌数
local current_tokens=math.min(capacity,(last_tokens+time_del*rate))
--3,判断是否允许访问
local acuire=0
if (requestNum <= current_tokens) then
    acuire=1
    current_tokens=(current_tokens-requestNum)
end

-- 计算一下过期时间 （默认就是填满令牌得时间*2）
local ttl = 60
--4，刷新记录
    redis.call('setex',token_key,ttl,current_tokens)
    redis.call('setex',time_key,ttl,now_time)

return { acuire }
