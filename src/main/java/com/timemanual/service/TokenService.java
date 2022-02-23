package com.timemanual.service;

import com.timemanual.dto.SessionUserInfo;

public interface TokenService {
   /**
    * 用户登录验证通过后(sso/帐密),生成token,记录用户已登录的状态
    */
   String generateToken(String username);

   SessionUserInfo getUserInfo();

   /**
    * 根据token查询用户信息
    * 如果token无效,会抛未登录的异常
    */
//   SessionUserInfo getUserInfoFromCache(String token);

//   void setCache(String token);

   /**
    * 退出登录时,将token置为无效
    */
   void invalidateToken(String token);

//   SessionUserInfo getUserInfoByUsername(String username);
}
