package com.timemanual.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class ShiroAuthToken implements AuthenticationToken {
    private String username;
    private String password;
    public ShiroAuthToken(String username, String password) {
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
}
