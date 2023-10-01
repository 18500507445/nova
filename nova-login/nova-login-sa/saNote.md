## nova-sa-token

## sa-token
> 轻量级Java权限认证框架，主要解决：登录认证、权限认证、单点登录、OAuth2.0、分布式Session会话、微服务网关鉴权等一系列权限相关问题。

### 登录方法
~~~java
// 当前会话注销登录
StpUtil.logout();

// 获取当前会话是否已经登录，返回true=已登录，false=未登录
StpUtil.isLogin();

// 检验当前会话是否已经登录, 如果未登录，则抛出异常：`NotLoginException`
StpUtil.checkLogin();
~~~

### 注解鉴权
~~~
@SaCheckLogin: 登录校验 —— 只有登录之后才能进入该方法。
@SaCheckRole("admin"): 角色校验 —— 必须具有指定角色标识才能进入该方法。
@SaCheckPermission("user:add"): 权限校验 —— 必须具有指定权限才能进入该方法。
@SaCheckSafe: 二级认证校验 —— 必须二级认证之后才能进入该方法。
@SaCheckBasic: HttpBasic校验 —— 只有通过 Basic 认证后才能进入该方法。
@SaIgnore：忽略校验 —— 表示被修饰的方法或类无需进行注解鉴权和路由拦截器鉴权。
@SaCheckDisable("comment")：账号服务封禁校验 —— 校验当前账号指定服务是否被封禁。
~~~

### 收集的知识
* [《百度》](https://www.baidu.com)

