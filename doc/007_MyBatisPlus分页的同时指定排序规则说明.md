

## MyBatis Plus要想使用分页，需要添加如下配置：
```java
@Configuration
@ConditionalOnClass(value = {PaginationInterceptor.class})
public class MybatisPlusConfig {
 
 /**
 * 分页插件
 * @return
 */
 @Bean
 public PaginationInterceptor paginationInterceptor() {
 PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
 return paginationInterceptor; //返回分布拦截器
 }
 
}
```

## 指定排序规则：
```java
public PageBean<Category> listPage(int pageNum, int pageSize) {
 IPage<Category> page = new Page<>(pageNum,pageSize);
 QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
 queryWrapper.eq("state",1)
 .orderByDesc("level");
 IPage<Category> categoryIPage = categoryMapper.selectPage(page, queryWrapper);
 return PageBean.init(categoryIPage);
}
```

```
public PageBean<Category> listPage(int pageNum, int pageSize) {
 IPage<Category> page = new Page<>(pageNum,pageSize);
 QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
 queryWrapper.eq("state",1)
 .orderBy("level")
 .last("desc"); // 手动把sql拼接到最后(有sql注入的风险,请谨慎使用)
 IPage<Category> categoryIPage = categoryMapper.selectPage(page, queryWrapper);
 return PageBean.init(categoryIPage);
}
```


service层
```
Page<UserVO> page = new Page<>(pageNum, pageSize); // 构建分页对象
page.setOrders(List<OrderItem>); // 设入排序项
userMapper.selectList(page); // 调用mybatis进行查询
```
dao
```
IPage<UserVO> selectList(page);
```