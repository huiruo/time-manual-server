package com.timemanual.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.timemanual.entity.Users;

import java.util.List;

public interface UsersDao extends BaseMapper<Users> {
    // 查询用户
    Users findByAccount(String account);
    // 注册
    Users register(Users user);
    // 查询昵称
    Users findByNickname(String nikename);
    // 查询
    List<Users> findAllUser();

//    void save(Users users);
}
