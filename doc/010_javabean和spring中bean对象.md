
## 参考：
https://blog.csdn.net/qq_41932224/article/details/121760254
```
JavaBeans是可重用的Java软件组件，可以在构建器工具中进行可视化操作。实际上，它们是用符合特定约定的Java编程语言编写的类

从上文来看，你可能感觉JavaBean是一个类，其实这段话后面还有一段：
JavaBean是可序列化的Java对象，具有空构造函数，并允许使用getter和setter方法访问属性
为了充当JavaBean类，对象类必须遵守关于方法命名、构造和行为的某些约定。这些约定使得拥有可以使用、重用、替换和连接JavaBeans的工具成为可能

综合上面的描述可以得出，一个JavaBean其实本质上是一个Java对象，为了使得这个Java对象满足要求，在编写其对应的类（JavaBean Class）的时候，该类需要满足几点要求：可序列化、空参构造器、getter和setter方法。

其他说法--->javaBean是类：
javabean其实就是有一定规范的java实体类，跟普通类差不多，不同的是类内部提供了一些公共的方法以便外界对该对象内部属性进行操作，比如set、get操作



一个JavaBean是一个遵循了一定协议（可序列化、空参构造器、getter和setter方法）的Java对象、

SpringBean就是由Spring IOC容器管理的对象。

什么是SpringBean?
bean是由SpringIOC容器实例化、组装和管理的对象。
```

## 前言
一.spring Bean是什么在传统的java应用中，bean的生命周期很简单。使用java关键字new进行bean实例化，然后该bean就可以使用了。

一旦这个bean不再使用，则有java自动进行垃圾回收。

相比之下，Spring容器中的bean的生命周期就显得相对复杂多了。简单地说：SpringBean是受Spring管理的对象

#### 二.Spring中如何创建Bean
```
在Spring中，Bean的创建是由Spring容器进行的。也就是说，在Spring中使用Bean的时候，不是由关键字New来创建实例。
```

#### 三.Spring中Bean装配方式
```
1.自动装配
2.使用XML装配
3.使用java装配
```

#### 四.Spring Bean与javaBean区别：

规范的不同：
```
传统的java应用中，javabean遵循一些规范，规范如下:

1、这个类必须具有一个公共的(public)无参构造函数；
2、所有属性私有化（private）；
3、私有化的属性必须通过public类型的方法（getter和setter）暴露给其他程序，并且方法的命名也必须遵循一定的命名规范
4、这个类应是可序列化的。（比如可以实现Serializable 接口，用于实现bean的持久性）

Spring容器对Bean没有特殊要求，不像javaBean一样遵循一些规范（不过对于通过设置方法注入的Bean，一定要提供setter方法）
```

用处的不同:
```
传统javaBean更多地作为值传递参数，
而Spring中的bean用处几乎无处不在，任何组件都可以被称其bean。
```

生命周期：
```
在传统的java应用中，bean的生命周期很简单。
使用java关键字new进行bean实例化，然后该bean就可以使用了，一旦该bean不再使用，则由java自动进行垃圾回收。

Spring中的Bean由Spring容器管理其生命周期行动，较为复杂。
```

#### 其他不同
```
传统java：子类是父类的加强，是一种特殊的父类。
Spring中bean：子bean和父bean可以是不同的类型。
```

```
传统java：继承是类与类之间的关系，主要表现为方法及属性的延续。
Spring中的bean：Spring中的bean的继承是实例之间关系，主要表现为参数值得延续。
```

```
传统java：子类实例完全当成父类实例使用。
Spring中bean：子bean不可作父bean使用。
```

