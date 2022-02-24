package com.timemanual.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.timemanual.config.exception.CommonJsonException;
import com.timemanual.dao.Login2Dao;
import com.timemanual.dto.SessionUserInfo;
import com.timemanual.service.TokenService;
import com.timemanual.util.StringTools;
import com.timemanual.util.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    @Autowired
    Cache<String, SessionUserInfo> cacheMap;

    @Autowired
    Login2Dao login2Dao;

    /**
     * 用户登录验证通过后(sso/帐密),生成token,记录用户已登录的状态
     */
    @Override
    public String generateToken(String username) {
        log.debug("1.generateToken");
        MDC.put("username", username);
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        //设置用户信息缓存
        setCache(token, username);
        return token;
    }

    @Override
    public SessionUserInfo getUserInfo() {
        String token = MDC.get("token");
        return getUserInfoFromCache(token);
    }

    /**
     * 退出登录时,将token置为无效
     */
    @Override
    public void invalidateToken() {
        String token = MDC.get("token");
        if (!StringTools.isNullOrEmpty(token)) {
            cacheMap.invalidate(token);
        }
        log.debug("退出登录,清除缓存:token={}", token);
    }

    /**
     * 根据token查询用户信息
     * 如果token无效,会抛未登录的异常
     */
    private SessionUserInfo getUserInfoFromCache(String token) {
        if (StringTools.isNullOrEmpty(token)) {
            throw new CommonJsonException(ErrorEnum.E_20011);
        }
        log.debug("2.根据token从缓存中查询用户信息,{}", token);
        SessionUserInfo info = cacheMap.getIfPresent(token);
        if (info == null) {
            log.info("没拿到缓存 token={}", token);
            throw new CommonJsonException(ErrorEnum.E_20011);
        }
        return info;
    }

    private void setCache(String token, String username) {
        SessionUserInfo info = getUserInfoByUsername(username);
        log.info("设置用户信息缓存:token={} , username={}, info={}", token, username, info);
        cacheMap.put(token, info);
    }

    private SessionUserInfo getUserInfoByUsername(String username) {

        log.info("1.getUserInfoByUsername------->");
        log.info(username);

        SessionUserInfo userInfo = login2Dao.getUserInfo(username);

        log.info("2.getUserInfoByUsername------->");
        log.info(String.valueOf(userInfo));

        // if (userInfo.getRoleIds().contains(1)) {
        if (userInfo.getRoleId()==1) {
            //管理员,查出全部按钮和权限码
            userInfo.setMenuList(login2Dao.getAllMenu());
            userInfo.setPermissionList(login2Dao.getAllPermissionCode());
        }
        return userInfo;
    }
}
