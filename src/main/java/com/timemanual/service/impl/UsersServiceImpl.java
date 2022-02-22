package com.timemanual.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.timemanual.dao.UsersDao;
import com.timemanual.entity.Users;
import com.timemanual.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

// （注意：在UserServiceImpl类，必须加上@Service注解，否则会报错 Field userService in com.xx.mybatisplus.controller.UserController required）
@Service
@Transactional
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersDao, Users> implements UsersService {
//public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Resource
    private UsersDao usersDao;

//    @Autowired
//    UsersService usersService;


    @Override
    public Users login(Users users) {
       Users userDb = usersDao.findByAccount(users.getAccount());
       if (userDb != null) {
            if (userDb.getPassword().equals(users.getPassword())) {

                log.info("插入---》");
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
        System.out.println("register"+users);

        Users userDb = usersDao.findByAccount(users.getAccount());
        log.info("test1");
        System.out.println(userDb);
        System.out.println(userDb == null);
        log.info("test2");
        if (userDb == null) {
            Users usersDbByNickname = usersDao.findByNickname(users.getNickname());
            if (userDb == null) {
                System.out.println("可以注册-hello123--->");

                usersDao.insert(users);

                return users;
            }
            throw new RuntimeException("昵称被使用");

        } else {
            log.info("register3:"+userDb.getNickname());
            System.out.println("register3:");
            if(userDb.getAccount().equals(users.getAccount())){

                log.info("register3:"+userDb.getNickname());
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
