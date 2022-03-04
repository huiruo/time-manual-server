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
    // token到期时间5分钟，毫秒为单位
    // public static final long EXPIRE_TIME = 5*60*1000;
    public static final long EXPIRE_TIME = 1*60*1000;
    // RefreshToken到期时间为30分钟，秒为单位
    public static final long REFRESH_EXPIRE_TIME = 30*60;
    private static final String TOKEN_SECRET = "ljdyaishijin**3nkjnj??";  //密钥盐

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


    /**
     * token验证
     */
     public static Boolean verify(String token){
        /*
        // 根据密码生成JWT效验器
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).withClaim(CLAIM_NAME, username).build();
        // 效验TOKEN
        DecodedJWT jwt = verifier.verify(token);
        log.debug("jwtUtil---verify success,过期时间:{}",jwt.getExpiresAt());
        return true;
         */

        /*
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT decodedJWT=jwtVerifier.verify(token);
            log.debug("verify---认证通过：");
            log.debug("verify---"+CLAIM_NAME+":",decodedJWT.getClaim(CLAIM_NAME).asString());
            log.debug("verify---过期时间：{}",decodedJWT.getExpiresAt());
            return true;
        */
         try {
             JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
             DecodedJWT decodedJWT=jwtVerifier.verify(token);
             log.debug("verify---认证通过：");
             log.debug("verify---"+CLAIM_NAME+":",decodedJWT.getClaim(CLAIM_NAME).asString());
             log.debug("verify---过期时间：{}",decodedJWT.getExpiresAt());
             return true;
         }catch (Exception e){
             return false;
         }
         /*
         try {
            //创建token验证器
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT decodedJWT=jwtVerifier.verify(token);
            log.debug("verify---认证通过：");
            log.debug("verify---"+CLAIM_NAME+":",decodedJWT.getClaim(CLAIM_NAME).asString());
            log.debug("verify---过期时间：{}",decodedJWT.getExpiresAt());
            return true;
        } catch (TokenExpiredException exception) {
            log.debug("jwtUtil---verify 过期:{}",exception.getMessage());
            // throw new RuntimeException("未查找到数据");
            // throw new RuntimeException("verify:过期");
            return false;
        } catch (SignatureVerificationException exception) {
            log.debug("jwtUtil---verify token错误:{}",exception.getMessage());
            // throw new RuntimeException("verify:token错误");
            return false;
        } catch (JWTDecodeException exception) {
            log.debug("jwtUtil---verify token解码错误:{}",exception.getMessage());
            // throw new RuntimeException("verify:token解码错误");
            return false;
        } catch (JWTVerificationException exception) {
            log.debug("jwtUtil---verify error 2:{}",exception.getMessage());
            // throw new RuntimeException("verify:error 2");
            return false;
        }
        */
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
