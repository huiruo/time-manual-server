

#### AccessToken及RefreshToken概念说明
```
1. AccessToken：用于接口传输过程中的用户授权标识，客户端每次请求都需携带，出于安全考虑通常有效时长较短。

2. RefreshToken：与AccessToken为共生关系，一般用于刷新AccessToken，保存于服务端，客户端不可见，有效时长较长
```

#### 关于Redis中保存RefreshToken信息(做到JWT的可控性)
```
1.登录认证通过后返回AccessToken信息(在AccessToken中保存当前的时间戳和帐号)，

	同时在Redis中设置一条以帐号为Key，Value为当前时间戳(登录时间)的RefreshToken，

	现在认证时必须AccessToken没失效以及Redis存在所对应的RefreshToken，

	且RefreshToken时间戳和AccessToken信息中时间戳一致才算认证通过，这样可以做到JWT的可控性，如果重新登录获取了新的AccessToken，旧的AccessToken就认证不了，

	因为Redis中所存放的的RefreshToken时间戳信息只会和最新的AccessToken信息中携带的时间戳一致，这样每个用户就只能使用最新的AccessToken认证。
```


```
2. Redis的RefreshToken也可以用来判断用户是否在线，如果删除Redis的某个RefreshToken，那这个RefreshToken所对应的AccessToken之后也无法通过认证了，就相当于控制了用户的登录，可以剔除用户
```
