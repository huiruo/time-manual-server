

```java
package com.sh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
 @TableId(value = "id",type = IdType.AUTO)
 private Integer id;
 private String name;
 private Integer age;
 private String email;
 private Date createTime;
 private Date updateTime;
}
```

```java
package com.sh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sh.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

}
```

```java
package com.sh.controller;

import com.sh.entity.User;
import com.sh.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class UserController {
 @Autowired
 private UserMapper userMapper;
 //查询 查询所以 null 就是没有条件
 @GetMapping("/selectUser")
 public List<User> getAll(){
  List<User> user = userMapper.selectList(null);
  return user;
 }
 //根据id查询
 @GetMapping("/selectById")
 public User selectUserById(){
  User user = userMapper.selectById(1);
  return user;
 }
 //根据多个id查询 Arrays.asList集合
 @GetMapping("/selectByIds")
 public List<User> selectUserByIds(){
  List<User> user = userMapper.selectBatchIds(Arrays.asList(1,2,3));
  return user;
 }
 //添加
 @PostMapping("/insertUser")
 public Integer insertUsers(User user){
  Integer result = userMapper.insert(user);
  return result;
 }
 //修改 根据id
 @PutMapping("/updateUser")
 public Integer updateUsers(User user){
  Integer result = userMapper.updateById(user);
  return result;
 }
 //删除 根据id
 @DeleteMapping("/deleteUser")
 public Integer deleteUsers(Integer id){
  Integer result = userMapper.deleteById(id);
  return result;
 }
}
```