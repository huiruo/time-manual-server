
## bean 理解
```
1、Java面向对象，对象有方法和属性，那么就需要对象实例来调用方法和属性（即实例化）；

2、凡是有方法或属性的类都需要实例化，这样才能具象化去使用这些方法和属性；

3、规律：凡是子类及带有方法或属性的类都要加上注册Bean到Spring IoC的注解；（@Component , @Repository , @ Controller , @Service , @Configration）

4、把Bean理解为类的代理或代言人（实际上确实是通过反射、代理来实现的），这样它就能代表类拥有该拥有的东西了

5、我们都在微博上@过某某，对方会优先看到这条信息，并给你反馈，那么在Spring中，你标识一个@符号，那么Spring就会来看看，并且从这里拿到一个Bean（注册）或者给出一个Bean（使用）
```
## 二、注解分为两类：
```
1、一类是使用Bean，即是把已经在xml文件中配置好的Bean拿来用，完成属性、方法的组装；

比如@Autowired , @Resource，可以通过byTYPE（@Autowired）、byNAME（@Resource）的方式获取Bean；


2、一类是注册Bean,@Component , @Repository , @Controller , @Service , @Configration这些注解都是把你要实例化的对象转化成一个Bean，放在IoC容器中，等你要用的时候，它会和上面的@Autowired , @Resource配合到一起，把对象、属性、方法完美组装。
```

## 关于@Bean
1、原理是什么？先看下源码中的部分内容：

```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    @AliasFor("name")
    String[] value() default {};
    @AliasFor("value")
    String[] name() default {};
    Autowire autowire() default Autowire.NO;
    String initMethod() default "";
    String destroyMethod() default AbstractBeanDefinition.INFER_METHOD;
}
```

Spring引入Autowire（自动装配）机制就是为了解决标签下标签过多的问题，标签过多会引发两个问题：
```
如果一个Bean中要注入的对象过多，比如十几二十个（这是很正常的），那将导致Spring配置文件非常冗长，可读性与维护性差

如果一个Bean中要注入的对象过多，配置麻烦且一不小心就容易出错
```

default-autowire有四种取值：
```
no：默认，即不进行自动装配，每一个对象的注入比如依赖一个标签

byName：按照beanName进行自动装配，使用setter注入

byType：按照bean类型进行自动装配，使用setter注入

constructor：与byType差不多，不过最终属性通过构造函数进行注入
```

通过测试类，可以看到这个注解的
```java
@Configuration
public class ConfigTest {

    @Bean(name = "config_test")
    public User user () {
        User user = new User();
        user.setAuthor("liu big big");
        user.setName("刘大大");
        return user;
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.register(ConfigTest.class);
        ac.refresh();
        User user = (User) ac.getBean("config_test");
        System.out.println(user.getAuthor());
        System.out.println(user.getName());
    }
}
```
