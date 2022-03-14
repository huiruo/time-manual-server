package com.timemanual.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtil {
    // public static final long EXPIRE_TIME = 30*1000;
    // token到期时间5分钟，毫秒为单位
    public static final long EXPIRE_TIME = 5*60*1000;
    // RefreshToken到期时间为30分钟，秒为单位
    public static final long REFRESH_EXPIRE_TIME = 30*60;
    // 密钥盐
    private static final String TOKEN_SECRET = "ljdyaishijin**3nkjnj??";
    private static final String CLAIM_NAME = "username";
    // private static final String CLAIM_NAME="account";
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";


    /**
     * 生成token
     */
    public static String sign(String account,Long currentTime){

        String token=null;
        try {
            Date expireAt=new Date(currentTime+EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("auth0")//发行人
                    .withClaim(CLAIM_NAME,account)//存放数据
                    .withClaim("currentTime",currentTime)
                    .withExpiresAt(expireAt)//过期时间
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (IllegalArgumentException|JWTCreationException je) {

        }
        return token;
    }

    //获取过期时间
    public static Long getExpire(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("currentTime").asLong();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * token验证
     */
     public static Boolean verify(String token){
         try {
             JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
             DecodedJWT decodedJWT=jwtVerifier.verify(token);
             log.debug("verify---"+CLAIM_NAME+":",decodedJWT.getClaim(CLAIM_NAME).asString());
             log.debug("verify---认证通过,过期时间：{}",decodedJWT.getExpiresAt());
             return true;
         } catch (TokenExpiredException e) {
            log.debug("jwtUtil---verify 过期:{}",e.getMessage());
            // throw new TokenExpiredException("token过期，重新登陆");
            return false;
        } catch (SignatureVerificationException e) {
            log.debug("jwtUtil---verify token错误:{}",e.getMessage());
            return false;
        } catch (JWTDecodeException e) {
            log.debug("jwtUtil---verify token解码错误:{}",e.getMessage());
            return false;
        } catch (JWTVerificationException e) {
            log.debug("jwtUtil---verify error 2:{}",e.getMessage());
            return false;
        } catch (Exception e){
            log.debug("verify---认证失败：{}",e.getMessage());
            return false;
        }
    }

    public static String getAccount(String token){
        try{
            DecodedJWT decodedJWT=JWT.decode(token);
            return decodedJWT.getClaim(CLAIM_NAME).asString();

        }catch (JWTCreationException e){
            return null;
        }
    }

    public static Long getCurrentTime(String token){
        try{
            DecodedJWT decodedJWT= JWT.decode(token);
            return decodedJWT.getClaim("currentTime").asLong();

        }catch (JWTCreationException e){
            return null;
        }
    }
}
