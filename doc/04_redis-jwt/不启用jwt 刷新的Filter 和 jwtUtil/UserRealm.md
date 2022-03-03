
#### src\main\java\com\timemanual\config\shiro\UserRealm.java
```java
   @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) {
        log.info("====================Token认证====================");
        String token = (String) auth.getCredentials();

        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);

        log.debug("doGetAuthenticationInfo 1:{}",token);
        log.debug("doGetAuthenticationInfo 2:{}",username);

        if (username == null) {
            log.debug("doGetAuthenticationInfo 2-1:{}","token invalid");
            // throw new AuthenticationException("token invalid");
            return null;
        }

        SysUser user = loginService.checkLoginUser(username);

        log.debug("doGetAuthenticationInfo 3:{}",user);

        if (user == null) {
            log.debug("doGetAuthenticationInfo 3-1:");
            // throw new AuthenticationException("User didn't existed!");
            return null;
        }

        if (!JwtUtil.verify(token, username, user.getPassword())) {
            log.debug("doGetAuthenticationInfo 4:{}","Username or password error");
            return null;
            // throw new RuntimeException("doGetAuthenticationInfo: Exception");
        }

        log.debug("doGetAuthenticationInfo 4-2:{}","success");

        return new SimpleAuthenticationInfo(token, token, getName());
    }
```
