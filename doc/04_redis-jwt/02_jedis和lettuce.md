

```
在SpringBoot2.x之后，redis依赖的底层实现用lettuce替换掉了jedis

jedis：采用的直连，多个线程操作的话，是不安全的；想要避免不安全，使用jedis pool连接池。更像BIO模式

lettuce：采用netty，实例可以在多个线程中共享，不存在线程不安全的情况；可以减少线程数量。更像NIO模式
```
