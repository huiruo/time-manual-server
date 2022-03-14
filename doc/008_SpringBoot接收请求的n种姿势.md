https://blog.csdn.net/qq_33591903/article/details/104019817

#  @PathVariable、@RequestParam与@RequestBody注解三者的区别

另外，get请求的请求头没有Content-Type字段。
```
如果前端传入Content-Type:application/json格式的数据，直接使用@RequestBody注解将json字符串转化为对象。

如果前端传入Content-Type:application/x-www-form-urlencoded格式的数据，如果能够得出方法参数具有的属性和请求参数一样的属性时，则不需要@RequestParam注解。例如注入到Map中，则需要@RequestParam注解。

如果后端已经使用了@RequestBody注解，代表只接收application/json类型的数据，此时若再传入application/x-www-form-urlencoded类型的数据，则后台会报错
```


## 首先定义一个User实体类：
```java
@Data
class User {
    String name;
    int age;
 
    User() {
    }
 
    User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

# 一、接收get请求
后端用实体类接收

前端则调用url：localhost:8080/user/loginByUser?name=tom&age=12
```java
    @GetMapping("/loginByUser")
    public User loginByUser(User user) {
        return user;
    }
```

#### （2）后端用参数接收
前端则调用url：localhost:8080/user/loginByParam?name1=tom
```java
    @GetMapping("/loginByParam")
    public User loginByParam(@RequestParam("name1") String name, @RequestParam(value = "age", required = true, defaultValue = "20") int age) {
        return new User(name, age);
    }
```

@RequestParam注解将请求参数绑定到方法参数上。它有以下3个常用参数
```
value：用来声明请求参数中的参数名称。例子中将请求参数中的name1绑定到方法参数中的name字段。
required：当没声明其required时，默认是true。即如果前端没传入name1的话，后端则会报错。
defaultValue：当age参数的required=true时，而前端又没有传入这个参数时，则参数列表中的这个age将会有一个默认值。
```
此时情况下的@RequestParam注解，可加可不加。

#### （3）后端使用Map接收
前端则调用url：localhost:8080/user/loginByMap?name=tom&age=12

值得注意的是，这里的map参数前需要加@RequestParam注解，用于将请求参数注入到map中。
```java
    @GetMapping("/loginByMap")
    public User loginByMap(@RequestParam Map<String, Object> map) {
        String name = (String) map.get("name");
        int age = Integer.parseInt((String) map.get("age"));
        return new User(name, age);
    }
```

#### （4）后端用路径接收
```java
    @GetMapping("/loginByPath/{name}/{age}")
    public User loginByPath(@PathVariable("name") String name, @PathVariable("age") int age) {
        return new User(name, age);
    }
```

#### （5）后端用数组接收
前端则调用url：localhost:8080/user/array?a=1&a=2&a=3

当然，这里也可用List<Integer>来接收，不过需要加上@RequestParam("a")注解

如果直接使用List<Integer>来接收，也不加上@RequestParam("a")注解的话，则会报错

No primary or default constructor found for interface java.util.List
```java

    @GetMapping("/array")
    public Integer[] array(Integer[] a) {
        return a;
    }
```


# 二、接收Post请求
注：如果对Content-Type不了解的同学，请先移步到我的另外一篇文章Content-Type详解

（1）后端使用实体类进行接收，前端传入Content-Type:application/json格式的数据
https://blog.csdn.net/qq_33591903/article/details/104036620

#### （1）后端使用实体类进行接收，前端传入Content-Type:application/json格式的数据
```java
    @PostMapping("/loginByUser")
    public User loginByUser(@RequestBody User user) {
        return user;
    }
```
@RequestBody注解用于将请求体中的json字符串转化为java对象。
值得注意的是
```
由于get无请求体，那么@RequestBody不能使用在get请求上。

@RequestBody与@RequestParam可以同时使用，@RequestBody最多只能有一个，而@RequestParam可以有多个。
```

如果这里的User对象，只有一个参数，比如name。那么这里也可以直接这样接收
```java
    @PostMapping("/loginByUser")
    public User loginByUser(@RequestBody String name) {
        return user;
    }
```

#### （2）后端使用实体类进行接收，前端传入Content-Type:application/x-www-form-urlencoded格式的数据
```java
    @PostMapping("/loginByUser")
    public User loginByUser(User user) {
        return user;
    }
```
Content-Type:application/x-www-form-urlencoded格式的数据，数据会以key/value格式进行传输，SpringMvc会直接将请求体中的参数直接注入到对象中。


#### （3）后端使用参数进行接收，前端传入Content-Type:application/x-www-form-urlencoded格式的数据
```java
    @PostMapping("/loginByParam")
    public User loginByParam(@RequestParam("name1") String name,
                             @RequestParam(value = "age", required = true, defaultValue = "20") int age) {
        return new User(name, age);
    }
```
此时的@RequestParam注解加不加都无所谓

#### （4）后端使用Map来接收，前端传入Content-Type:application/x-www-form-urlencoded格式的数据
```java
    @PostMapping("/loginByMap")
    public User loginByMap(@RequestParam Map<String, Object> map) {
        String name = (String) map.get("name");
        int age = Integer.parseInt((String) map.get("age"));
        return new User(name, age);
    }

这里类似于get请求的(3)，同样，map参数前需要加@RequestParam注解，用于将请求参数注入到map中。

值得注意的是，由于form表单形式是以key/value形式存储，都是字符串类型，因此需要将map.get("age")转化为String，再转化为Integer，最后再自动拆箱。

不可以将map.get("age")直接转化为Integer类型，因为其本质是String类型，String不能直接强转为Integer。
```

#### （5）后端使用Map来接收，前端传入Content-Type:application/json格式的数据
```java
@PostMapping("/loginByMap")
    public User loginByMap(@RequestBody Map<String, Object> map) {
        String name = (String) map.get("name");
        int age = (Integer) map.get("age");
        return new User(name, age);
    }
```
这里类似于post请求的(1)，同样，@RequestBody注解用于将请求体中的json字符串转化为对象属性，并注入到map中。

由于请求体中json中的age类型为number类型，因此注入到map中时，age是Integer类型，那么可以直接强转为Integer类型。

#### （6）后端使用JSONObject来接收，前端传入Content-Type:application/json格式的数据
@RequestBody注解用于将请求体中的json字符串转化为JSON对象。
```java
    @PostMapping("/loginByJSONObject")
    public User loginByJSONObject(@RequestBody JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int age = jsonObject.getInteger("age");
        return new User(name, age);
    }
```

#### （7）后端使用数组来接收
前端传入Content-Type:application/x-www-form-urlencoded格式的数据，后端可以直接接收到。如图
```java
    @PostMapping("/array")
    public Integer[] array(Integer[] a) {
        return a;
    }
```

但传入Content-Type:application/json格式的数据[1,2,3]，后端则接收不到，需要加入@RequestBody注解。

当然(@RequestBody List<Integer> a)也是可以的。

