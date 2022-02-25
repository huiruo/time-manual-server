package com.timemanual.dao;

import com.alibaba.fastjson.JSONObject;
import com.timemanual.entity.SysUser;
import org.apache.ibatis.annotations.Param;

public interface LoginDao {
    /*
    如果要正确的传入参数，那么就要给参数命名，因为不用xml配置文件，那么我们就要用别的方式来给参数命名，这个方式就是@Param注解
    where  s_name= #{aaaa} and class_id = #{bbbb} ，表示sql语句要接受2个参数，一个参数名是aaaa，一个参数名是bbbb
    */

    // 在方法参数的前面写上@Param("参数名")，表示给参数命名，名称就是括号中的内容
    /*
    * public Student select(@Param("aaaa") String name,@Param("bbbb")int class_id);
　　 * 给入参 String name 命名为aaaa，然后sql语句....where  s_name= #{aaaa} 中就可以根据aaaa得到参数值了。
    * */
    /**
     * 根据用户名和密码查询对应的用户
     */
    JSONObject getUser(@Param("username") String username, @Param("password") String password);

    JSONObject checkUser(@Param("username") String username);

    SysUser checkLoginUser(@Param("username") String username);
}
