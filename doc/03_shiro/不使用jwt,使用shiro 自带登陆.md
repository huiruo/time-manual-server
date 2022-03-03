
### src\main\java\com\timemanual\service\impl\Login3ServiceImpl.java
```java
   @Override
    public JSONObject authLogin3(String username, String password) {
        JSONObject info = new JSONObject();
        Subject currentUser = SecurityUtils.getSubject();
        // UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        ShiroAuthToken token = new ShiroAuthToken(username, password);
        log.debug("authLogin3-->2,这个token包含很多用户信息:{}", token);

        try {
            currentUser.login(token);

            // String generateToken = tokenService.generateToken(username);
            // test 查询缓存信息
            // SessionUserInfo infoCacheMap = cacheMap.getIfPresent(generateToken);
            // log.debug("authLogin3-->3:{}",infoCacheMap);
            // test end

            String generateToken = JwtUtil.sign(username, password);
            Long currentTimeMillis = System.currentTimeMillis();
            redisUtil.set(username,currentTimeMillis,JwtUtil.REFRESH_EXPIRE_TIME);
            info.put("token", generateToken);
        } catch (UnknownAccountException e) {
            info.put("msg", "用户名有误");
        } catch (IncorrectCredentialsException e) {
            info.put("msg", "密码有误");
        }

        return CommonUtil.successJson(info);
    }
```
