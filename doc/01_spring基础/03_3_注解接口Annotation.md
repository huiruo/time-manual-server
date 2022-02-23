

## 所有的java注解类型都继承接口Annotation
## 2.获取注解的属性值

通过反射获取注解的属性值
```java
@Retention(RetentionPolicy.RUNTIME)
public @interface Vui {
    String name() default "";
    boolean isUniuqe() default false;
}
```

```java
@Vui(name = "zhang",isUniuqe = true)
public class TestAnn {

    public String getName() {
        System.out.println("hh");
        return "";
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        TestAnn testAnnotation  = new TestAnn();
        Annotation[] annotations = testAnnotation.getClass().getAnnotations();
        for (Annotation annotation:annotations) {
            Method[] methods = annotation.annotationType().getMethods();
            for (Method method:methods) {
                System.out.println(method.invoke(annotation));
            }
        }
    }
}
```