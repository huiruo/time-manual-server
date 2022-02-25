package com.timemanual.config.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/*
* 实现 AuthenticationToken 接口，作为 Token 传入到 Realm 的载体：
* */
public class JWTToken implements AuthenticationToken {
    /*
    private String username;
    private String password;
    public JWTToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
    */
//    */
    private static final long serialVersionUID = 2231225172429467901L;
    // TOKEN
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

}
