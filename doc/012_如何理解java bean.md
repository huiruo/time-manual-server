

## 前言
```
bean 豆角，扁豆

Java Bean 它是一种类，而且是特殊的、可重用的类。

Java language 是一种面向对象的编程语言，类是面向对象的编程语言的基础；可重用又是面向对象编程思想存在的意义之一，所以起名 Bean 很是形象。
```

```java
public class Person {
    privete String name;
    private int age;
    
    public void setName(String newName) {
        name = newName;
    }
    public String getName() {
        return name;
    }
    
    public void setAge(int neweAge) {
        age = newAge;
    }
    public int getAge() {
        return age;
    }
}
```

#### 并非所有的类都是 Java Bean，其是一种特殊的类，具有以下特征：
```
1.提供一个默认的无参构造函数。
2.需要被序列化并且实现了 Serializable 接口。
3.可能有一系列可读写属性，并且一般是 private 的。
4.可能有一系列的 getter 或 setter 方法
```

根据封装的思想，我们使用 get 和 set 方法封装 private 的属性，并且根据属性是否可读写来选择封装方法。

#### Java Bean 的作用
```
其最大的特征是私有的属性，getter 和 setter 方法也都是绕着这些属性来设计的。

想象一下存在这样一个箱子，其内部被分割成几个格子，每个格子用来存放特定的物品，工人取出或者放入物品后封箱，然后叫了个快递把箱子发出去了。这个箱子就是 Java Bean 啊，取出、放入就是getter、setter，物品就是属性，封箱发出就是序列化和传输。

那么 Java Bean 的作用也就是把一组数据组合成一个特殊的类便于传输。 Java Bean 可以用在图形界面的可视化设计、JSP 封装数据保存到数据库、Android AIDL 跨进程通信，Spring框架等场景中。
```
