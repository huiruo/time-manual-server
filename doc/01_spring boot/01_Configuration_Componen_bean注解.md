

```
@Configuration 用于定义配置类，可替换XML配置文件，被注解的类内部包含一个或多个@Bean注解方法。可以被AnnotationConfigApplicationContext或者AnnotationConfigWebApplicationContext 进行扫描。用于构建bean定义以及初始化Spring容器。
```

## @Configuration 加载Spring方法
Car.java
```java
public class Car {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
```
定义Config类
```java
@Configuration
public class Config {
    public Config() {
        System.out.println("TestConfig容器初始化...");
    }

    @Bean(name = "getMyCar")
    public Car getCar() {
        Car c = new Car();
        c.setName("dankun");
        return c;
    }
}
```

实例化
```java
public void testConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Car car = (Car)context.getBean("car");
        System.out.println(car.getName());
    }
// 输出
// TestConfig容器初始化...
// dankun
```

## @Configuration + @Component
@Configuration也附带了@Component的功能。所以理论上也可以使用@Autowared功能。上述代码可以改成下面形式

Car.java
```java
@Component
public class Car {
    @Value("dankun")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
```

Config.java
```java
@Configuration
@ComponentScan("com.wuyue.annotation")
public class Config {
    public Config() {
        System.out.println("TestConfig容器初始化...");
}
```

测试主入口
```java
public class TestConfig {
    @Test
    public void testConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Car car = (Car)context.getBean("car");
        System.out.println(car.getName());
    }
}
// 输出
// TestConfig容器初始化...
// dankun
```

## 总结
```
@Configuation等价于<Beans></Beans>

@Bean 等价于<Bean></Bean>

@ComponentScan等价于<context:component-scan base-package="com.dxz.demo"/>

@Component 等价于<Bean></Bean>
```

## @Bean VS @Component
```
两个注解的结果是相同的，bean都会被添加到Spring上下文中。

@Component 标注的是类,允许通过自动扫描发现。@Bean需要在配置类@Configuation中使用。

@Component类使用的方法或字段时不会使用CGLIB增强。而在@Configuration类中使用方法或字段时则使用CGLIB创造协作对象
```

## @Component和@Bean的目的是一样的，都是注册bean到Spring容器中。
```
@Component 和 它的子类型（@Controller, @Service and @Repository）注释在类上。告诉Spring，我是一个bean，通过类路径扫描自动检测并注入到Spring容器中。

@Bean不能注释在类上，只能用于在配置类中显式声明单个bean。意思就是，我要获取这个bean的时候，spring要按照这种方式去获取这个bean。默认情况下@Bean注释的方法名作为对象的名字，也可以用name属性定义对象的名字。
```

## @Component @Bean 区别
```
1、@Component注解表明一个类会作为组件类，并告知Spring要为这个类创建bean。

2、@Bean注解告诉Spring这个方法将会返回一个对象，这个对象要注册为Spring应用上下文中的bean。通常方法体中包含了最终产生bean实例的逻辑。

两者的目的是一样的，都是注册bean到Spring容器中。
```
区别:
```
1.@Component（@Controller、@Service、@Repository）通常是通过类路径扫描来自动侦测以及自动装配到Spring容器中。
而@Bean注解通常是我们在标有该注解的方法中定义产生这个bean的逻辑。

2.@Component 作用于类，@Bean作用于方法。

```
## 总结：
```
@Component和@Bean都是用来注册Bean并装配到Spring容器中，但是Bean比Component的自定义性更强。

可以实现一些Component实现不了的自定义加载类。
```