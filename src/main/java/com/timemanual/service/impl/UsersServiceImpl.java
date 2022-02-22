package com.timemanual.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timemanual.dao.UsersDao;
import com.timemanual.entity.Users;
import com.timemanual.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// （注意：在UserServiceImpl类，必须加上@Service注解，否则会报错 Field userService in com.xx.mybatisplus.controller.UserController required）
@Service
@Transactional
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersDao, Users> implements UsersService {

    @Autowired
    private UsersDao usersDao;

    @Override
    public Users login(Users users) {
       Users userDb = usersDao.findByAccount(users.getAccount());
       if (userDb != null) {
            if (userDb.getPassword().equals(users.getPassword())) {
                usersDao.insert(userDb);
                return userDb;
            } else {
                throw new RuntimeException("密码错误");
            }
       } else {
            throw new RuntimeException("用户名不存在");
       }
    }

    @Override
    public Users register(Users users) {
        Users userDb = usersDao.findByAccount(users.getAccount());
        if (userDb == null) {
            Users usersDbByNickname = usersDao.findByNickname(users.getNickname());
            if (userDb == null) {
                usersDao.insert(users);
                return users;
            }
            throw new RuntimeException("昵称被使用");
        } else {
            if(userDb.getAccount().equals(users.getAccount())){
                throw new RuntimeException("用户名被使用");
            }
            throw new RuntimeException("用户名被使用");
        }
    }

    @Override
    public List<Users> findAllUser() {
       return usersDao.findAllUser();
    }

    @Override
    public Users findByAccount(String account) {
        return usersDao.findByAccount(account);
    }

    @Override
    public Users findByNickname(String nikename) {
        return usersDao.findByNickname(nikename);
    }
}
