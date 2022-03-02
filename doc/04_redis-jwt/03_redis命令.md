## 安装服务：
```
https://github.com/tporadowski/redis
```

## 命令那么多
免费的可视化工具好用：
https://github.com/qishibo/AnotherRedisDesktopManager/releases
```

本地启动：redis-cli

QUIT 
关闭当前连接
```

### windows 设置密码
```
CONFIG SET requirepass "123456"

验证：
CONFIG GET requirepass
```

二、redis keys命令
```
1.DEL key
2.DUMP key 
序列化给定的key并返回序列化的值

2.exists key 
检查给定的key是否存在

3、EXPIRE key seconds 
为key设置过期时间

4、EXPIRE key timestamp 
用时间戳的方式给key设置过期时间

5、PEXPIRE key milliseconds 
设置key的过期时间以毫秒计

6、KEYS pattern 
查找所有符合给定模式的key

7、MOVE key db 
将当前数据库的key移动到数据库db当中

8、PERSIST key
移除key的过期时间，key将持久保存

9、PTTL key
以毫秒为单位返回key的剩余过期时间

10、TTL key
以秒为单位，返回给定key的剩余生存时间

11、RANDOMKEY
从当前数据库中随机返回一个key

12、RENAME key newkey
修改key的名称

13、RENAMENX key newkey
仅当newkey不存在时，将key改名为newkey

14、TYPE key
返回key所存储的值的类型
```

## reids字符串命令
```
1、SET key value

2、GET key

3、GETRANGE key start end
返回key中字符串值的子字符

4、GETSET key value
将给定key的值设为value，并返回key的旧值

5、GETBIT KEY OFFSET
对key所储存的字符串值，获取指定偏移量上的位

6、MGET KEY1 KEY2
获取一个或者多个给定key的值

7、SETBIT KEY OFFSET VALUE
对key所是存储的字符串值，设置或清除指定偏移量上的位

8、SETEX key seconds value
将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。

9、SETNX key value
只有在 key 不存在时设置 key 的值。

10、SETRANGE key offset value
用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始。

11、STRLEN key
返回 key 所储存的字符串值的长度。

12、MSET key value [key value ...]
同时设置一个或多个 key-value 对。

13、MSETNX key value [key value ...] 
同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。

14、PSETEX key milliseconds value
这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。

15、INCR key
将 key 中储存的数字值增一。

16、INCRBY key increment
将 key 所储存的值加上给定的增量值（increment） 。

17、INCRBYFLOAT key increment
将 key 所储存的值加上给定的浮点增量值（increment） 。

18、DECR key
将 key 中储存的数字值减一。

19、DECRBY key decrement
key 所储存的值减去给定的减量值（decrement） 。

20、APPEND key value
如果 key 已经存在并且是一个字符串， APPEND 命令将 指定value 追加到改 key 原来的值（value）的末尾。
```

## Redis hash 命令
```
1    HDEL key field1 [field2] 
删除一个或多个哈希表字段

2    HEXISTS key field 
查看哈希表 key 中，指定的字段是否存在。

3    HGET key field 
获取存储在哈希表中指定字段的值。

4    HGETALL key 
获取在哈希表中指定 key 的所有字段和值

5    HINCRBY key field increment 
为哈希表 key 中的指定字段的整数值加上增量 increment 。

6    HINCRBYFLOAT key field increment 
为哈希表 key 中的指定字段的浮点数值加上增量 increment 。

7    HKEYS key 
获取所有哈希表中的字段

8    HLEN key 
获取哈希表中字段的数量

9    HMGET key field1 [field2] 
获取所有给定字段的值

10    HMSET key field1 value1 [field2 value2 ] 
同时将多个 field-value (域-值)对设置到哈希表 key 中。

11    HSET key field value 
将哈希表 key 中的字段 field 的值设为 value 。

12    HSETNX key field value 
只有在字段 field 不存在时，设置哈希表字段的值。

13    HVALS key 
获取哈希表中所有值

14    HSCAN key cursor [MATCH pattern] [COUNT count] 
迭代哈希表中的键值对。
```

### Redis 列表命令
```
1    BLPOP key1 [key2 ] timeout 
移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。

2    BRPOP key1 [key2 ] timeout 
移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。

3    BRPOPLPUSH source destination timeout 
从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。

4    LINDEX key index 
通过索引获取列表中的元素

5    LINSERT key BEFORE|AFTER pivot value 
在列表的元素前或者后插入元素

6    LLEN key 
获取列表长度

7    LPOP key 
移出并获取列表的第一个元素

8    LPUSH key value1 [value2] 
将一个或多个值插入到列表头部

9    LPUSHX key value 
将一个值插入到已存在的列表头部

10    LRANGE key start stop 
获取列表指定范围内的元素

11    LREM key count value 
移除列表元素

12    LSET key index value 
通过索引设置列表元素的值

13    LTRIM key start stop 
对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。

14    RPOP key 
移除并获取列表最后一个元素

15    RPOPLPUSH source destination 
移除列表的最后一个元素，并将该元素添加到另一个列表并返回

16    RPUSH key value1 [value2] 
在列表中添加一个或多个值

17    RPUSHX key value 
为已存在的列表添加值
```

#### Redis 集合命令
```
1    SADD key member1 [member2] 
向集合添加一个或多个成员

2    SCARD key 
获取集合的成员数

3    SDIFF key1 [key2] 
返回给定所有集合的差集

4    SDIFFSTORE destination key1 [key2] 
返回给定所有集合的差集并存储在 destination 中

5    SINTER key1 [key2] 
返回给定所有集合的交集

6    SINTERSTORE destination key1 [key2] 
返回给定所有集合的交集并存储在 destination 中

7    SISMEMBER key member 
判断 member 元素是否是集合 key 的成员

8    SMEMBERS key 
返回集合中的所有成员

9    SMOVE source destination member 
将 member 元素从 source 集合移动到 destination 集合

10    SPOP key 
移除并返回集合中的一个随机元素

11    SRANDMEMBER key [count] 
返回集合中一个或多个随机数

12    SREM key member1 [member2] 
移除集合中一个或多个成员

13    SUNION key1 [key2] 
返回所有给定集合的并集

14    SUNIONSTORE destination key1 [key2] 
所有给定集合的并集存储在 destination 集合中

15    SSCAN key cursor [MATCH pattern] [COUNT count] 
迭代集合中的元素
```

#### Redis 有序集合命令

#### Redis 发布订阅命令

#### Redis 事务命令

#### Redis 脚本命令

#### Redis 服务器命令

