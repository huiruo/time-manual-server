## HashMap,LinkedHashMap,TreeMap 都属于Map
```
Map 主要用于存储键(key)值(value)对，根据键得到值，因此键不允许键重复,但允许值重复。
```

## 区别
```
LinkedHashMap是继承于HashMap，是基于HashMap和双向链表来实现的。

HashMap无序；LinkedHashMap有序，可分为插入顺序和访问顺序两种。如果是访问顺序，那put和get操作已存在的Entry时，都会把Entry移动到双向链表的表尾(其实是先删除再插入)。

LinkedHashMap存取数据，还是跟HashMap一样使用的Entry[]的方式，双向链表只是为了保证顺序。

LinkedHashMap是线程不安全的。
```


### HashMap
```
是一个最常用的Map,它根据键的HashCode 值存储数据,根据键可以直接获取它的值，具有很快的访问速度。HashMap最多只允许一条记录的键为Null;允许多条记录的值为 Null;HashMap不支持线程的同步，即任一时刻可以有多个线程同时写HashMap;可能会导致数据的不一致。如果需要同步，可以用 Collections的synchronizedMap方法使HashMap具有同步的能力。
```
### 好好谈谈HashMa
```
1，初始化大小是16，如果事先知道数据量的大小，建议修改默认初始化大小。 减少扩容次数，提高性能 ，这是我一直会强调的点
2，最大的装载因子默认是0.75，当HashMap中元素个数达到容量的0.75时，就会扩容。 容量是原先的两倍
3，HashMap底层采用链表法来解决冲突。 但是存在一个问题，就是链表也可能会过长，影响性能

于是JDK1.8,对HashMap做了进一步的优化，引入了红黑树。
当链表长度超过8，且数组容量大于64时，链表就会转换为红黑树
当红黑树的节点数量小于6时，会将红黑树转换为链表。
因为在数据量较小的情况下，红黑树要维护自身平衡，比链表性能没有优势。
这3点非常重要！

其次，LinkedHashMap就是链表+散列表的结构，其底层采用了Linked双向链表来保存节点的访问顺序，所以保证了有序性。
```

HashMap的例子
```java
public static void main(String[] args) {  

      Map<String, String> map = new HashMap<String, String>(); 

      map.put("a3", "aa");
      map.put("a2", "bb"); 
      map.put("b1", "cc");

      for (Iterator iterator = map.values().iterator(); iterator.hasNext();)     {

            String name = (String) iterator.next(); 

            System.out.println(name);   
     }  
}

输出：bbccaa
```

### LinkedHashMap
```
LinkedHashMap也是一个HashMap,但是内部维持了一个双向链表,可以保持顺序
```

```java
public static void main(String[] args) {   

     Map<String, String> map = new LinkedHashMap<String, String>();

     map.put("a3", "aa");       
     map.put("a2", "bb"); 
     map.put("b1", "cc"); 

     for (Iterator iterator = map.values().iterator(); iterator.hasNext();) {           

             String name = (String) iterator.next(); 

             System.out.println(name);     
     }
}

输出：
aa
bb
cc
```

### TreeMap 
```
TreeMap 可以用于排序
```

## 总结
```
总结归纳为:linkedMap在于存储数据你想保持进入的顺序与被取出的顺序一致的话，优先考虑LinkedMap，hashMap键只能允许为一条为空，value可以允许为多条为空，键唯一，但值可以多个。

经本人测试linkedMap键和值都不可以为空
```

