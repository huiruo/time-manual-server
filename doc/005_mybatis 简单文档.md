
https://blog.csdn.net/weixin_39771301/article/details/111700461
## 查询 <select>

之前只有一个根据id查询对象的方法,现在在mapper文件中加入常见的增删改查的方法,首先加入一个查询所有的方法,查询,在UserMapper.xml配置中,使用的都是<select></select>标签
```xml
<!-- 查询 t_user 表中所有用户数据 -->
<select id="getAll" resultType="com.yingside.bean.User">
    select * from t_user
</select>
```

## 3.添加关联映射
在Mapper文件中添加关联映射,这种方式是处理字段不匹配是最常用的手段
如果我们这么写了之后,你会发现打印的效果其实和之前一样,userTel和registrationTime得到的结果是null,其实原因是一样的,这是最好还是应该使用resultMap
```xml
<?xml version="1.0" encoding="UTF-8" ?><!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.yingside.mapper.UserMapper">
    <resultMap id="userMap" type="com.yingside.bean.User">
        <id column="id" property="id" />
        <result column="user_tel" property="userTel"/>
        <result column="username" property="username"/>
        <result column="password" property="password"/>
        <result column="registration_time" property="registrationTime"/>
    </resultMap>
 
    <select id="getUser" parameterType="int"
            resultMap="userMap">
        select * from t_user where id=#{id}
    </select>
</mapper>
```

## 4.新增数据返回主键id值
不过这里的结果是一般数据库DML语句表示几行受影响的结果,一般我们的新增操作要求都比较特殊,需要知道最新插入的数据主键id是多少,如果这样的话,可以在<insert>标签中加入下面的代码
```xml
<!-- 向 t_user 表插入一条数据 -->
<insert id="insertUser" parameterType="com.yingside.bean.User">
    <!--将插入的数据主键返回到 user 对象中
       keyProperty:将查询到的主键设置到parameterType 指定到对象的那个属性
       select LAST_INSERT_ID()：查询上一次执行insert 操作返回的主键id值，只适用于自增主键
       resultType:指定 select LAST_INSERT_ID() 的结果类型
       order:AFTER，相对于 select LAST_INSERT_ID()操作的顺序
    -->
    <selectKey keyProperty="id" resultType="int" order="AFTER">
        select LAST_INSERT_ID()    
    </selectKey>
    insert into t_user(id,user_tel,username,password,registration_time) value(null,#{userTel},#{username},#{password},#{registrationTime})
</insert>


当然上面的代码完全可以简写成下面这种方式:

<insert id="insertUser" parameterType="user" useGeneratedKeys="true" keyProperty="id"></insert>
```

## 5.mysql数据库是支持主键自增id的,上面这种办法,也只是主键自增id的写法,如果是非自增主键机制,比如使用UUID,就需要换一种写法
```xml
<insert id="insertUser" parameterType="com.yingside.bean.User">
    <!-- 将插入的数据主键返回到 user 对象中
         首先通过 select UUID()得到主键值，然后设置到 user 对象的id中，再进行 insert 操作
         keyProperty: 将查询到的主键设置到parameterType 指定到对象的那个属性
         select UUID()：得到主键的id值，注意这里是字符串
         resultType: 指定 select UUID() 的结果类型
         order: BEFORE，相对于 select UUID()操作的顺序
         注意先后顺序和自增主键机制是不一样的
     -->
    <selectKey keyProperty="id" resultType="String" order="BEFORE">
        select UUID()    </selectKey>
    insert into t_user(id,user_tel,username,password,registration_time)
        value(#{id},#{userTel},#{username},#{password},#{registrationTime})
</insert>
```

## 6.删除 <delete>
```xml
<!-- 根据 id 删除 user 表的数据 -->
<delete id="deleteUserById" parameterType="int">
    delete from t_user where id=#{id}
</delete>
```

## 7.更新 <update>
更新使用<update>标签,一般情况下,是根据主键id,更新数据
```xml
<!-- 根据 id 更新 user 表的数据 -->
<update id="updateUserById" parameterType="com.yingside.bean.User">
  update t_user set user_tel=#{userTel},username=#{username},
  password=md5(#{password}),registration_time=#{registrationTime}
  where id=#{id}
</update>
```

## 8.配置别名<typeAliases>
这就是最基本的数据库CRUD操作,不过上面的xml配置中,有一个的重复很烦躁,那就是 parameterType="com.yingside.bean.User"如果是自定义的类型,就必须跟上类的包名全路径,那这个其实只需要在全局环境中配置一下别名就可以了

```xml
<configuration>
   ...    
   <typeAliases>
        <!--给com.yingside.bean包下的所有类起一个别名,默认就是类名 com.yingside.bean.User === User
        (注意低版本的MyBatis 别名默认是类名首字母小写,相当于 com.yingside.bean.User === user)
        这样在每个mapper文件中如果出现自定义的类,就不必再写成 包路径 + 类名 的形式了,直接写这里定义的别名就ok
        -->
        <package name="com.yingside.bean"/>
        <!--单独的给每个类别名-->
        <!--<typeAlias type="com.yingside.bean.User" alias="user" />-->
   </typeAliases>
    ... 
</configuration>
```


## Mybatis-Plus 忽略部分返回字段
```
  @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    @TableField("create_date")
    private Date createDate;
```