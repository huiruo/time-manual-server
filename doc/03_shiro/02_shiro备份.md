
## doGetAuthenticationInfo

```java
/*
 * 当前用户调用login()方法后，抛出UnknownAccountException异常，就说明用户名不存在，
 * 抛出IncorrectCredentialsException异常，就说明用户密码错误，若不出现异常，则登录验证成功。
 * 只要调用了当前对象的login()方法，Shiro就会找到自定义的Realm，
 * 执行它的认证逻辑方法：doGetAuthenticationInfo(),所以我们可以在自定义Realm中进行一些相关的逻辑操作。
 * */

/*
* 第一个字段：我自己测试的，可以传username，也可以传user对象
* 第二个字段是user.getPassword()，注意这里是指从数据库中获取的password。
* 第三个字段是realm，即当前realm的名称。
* 这块对比逻辑是先对比username，但是username肯定是相等的，所以真正对比的是password。从这里传入的password（这里是从数据库获取的）和
* token（filter中登录时生成的）中的password做对比，如果相同就允许登录，不相同就抛出异常。
* token的password不用传，在realm中对比的时候能够获取到token这个对象，那时候是可以获取到token中所有的信息的。另外这里还可以传salt这个参数
* SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getString("password"), getName());
* */
@Override
protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {

		log.debug("doGetAuthenticationInfo--->进行用户登陆验证");

		// 获取 authcToken 用户和密码
		String loginName = (String) authToken.getPrincipal();
		// String password = new String((char[]) authcToken.getCredentials());

		JSONObject user = loginService.checkUser(loginName);

		log.debug("doGetAuthenticationInfo--->2:{}",loginName);
		// log.debug("doGetAuthenticationInfo--->2:{}",password);
		log.debug("doGetAuthenticationInfo--->3:{}",user);

		// test
		// login2Service.authLogin2(loginName,password);
		// test end

		// 自定义token test start
		// String accessToken = (String) authToken.getPrincipal();
		// //根据accessToken，查询用户信息
		// SysUserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
		// ShiroAuthTokenTest shiroAuthToken = (ShiroAuthTokenTest) authToken;
		// String token = (String) shiroAuthToken.getCredentials();

		// 验证 Token
		//        SysUser sysUser = tokenUtils.validationToken(token);
		//        if (sysUser == null || sysUser.getUsername() == null || sysUser.getUserRole() == null) {
		//            throw new AuthenticationException("Token 无效");
		//        }
		// 从 token 中获取用户输入的身份凭证
		ShiroAuthToken shiroAuthToken = (ShiroAuthToken) authToken;
		String userName = (String) shiroAuthToken.getPrincipal();

		logger.debug("自定义：{}",userName);

		// 自定义token test end

		if (user == null) {
				// 没找到帐号
				throw new UnknownAccountException();
		}

		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，也可以可以自定义实现
		//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
		//                user.getString("username"),
		//                user.getString("password"),
		//                //ByteSource.Util.bytes("salt"), salt=username+salt,采用明文访问时，不需要此句
		//                getName()
		//        );

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getString("password"), getName());


		// session中不需要保存密码
		user.remove("password");
		//将用户信息放入session中
		SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_USER_INFO, user);
		log.debug("doGetAuthenticationInfo--->5-1:{}",user);
		log.debug("doGetAuthenticationInfo--->5-2:{}",info);

		// 注意：最后的return simpleAuthenticationInfo 的时候就会触发password验证
		return info;
}
```
