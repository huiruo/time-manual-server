package com.timemanual.config.jwt;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTUtil {
    // 5 分钟后过期
    // private static final long EXPIRE_TIME = 5 * 60 * 1000;

    private static final long EXPIRE_TIME = 60 * 1000;
    // private static final long EXPIRE_TIME = 30 * 1000;
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 验证 token 是否正确
     *
     * @param token
     * @param username
     * @param secret
     * @return boolean
     */
    public static boolean verify(String token, String username, String secret) {
        String realToken = token;
        if(token.startsWith(TOKEN_PREFIX)){
            String[] tokenString = token.split(" ");
            realToken = tokenString[1];
        }else {
            log.debug("不需要截取token");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            @SuppressWarnings("unused")
            DecodedJWT jwt = verifier.verify(realToken);
            log.debug("jwtUtil---verify success:{}");
            return true;
        } catch (TokenExpiredException exception) {
            log.debug("jwtUtil---verify 过期:{}",exception.getMessage());
            return false;
        } catch (SignatureVerificationException exception) {
            log.debug("jwtUtil---verify token错误:{}",exception.getMessage());
           return false;
        } catch (JWTDecodeException exception) {
            log.debug("jwtUtil---verify token解码错误:{}",exception.getMessage());
            return false;
        } catch (JWTVerificationException exception) {
            log.debug("jwtUtil---verify error 2:{}",exception.getMessage());
            return false;
        }
    }

    /**
     * 从 token 中获取用户信息
     *
     * @param token
     * @return username
     */
    public static String getUsername(String token) {
        try {
            String realToken = token;
            if(token.startsWith(TOKEN_PREFIX)){
                String[] tokenString = token.split(" ");
                realToken = tokenString[1];
            }else {
                log.debug("不需要截取token-2");
            }
            DecodedJWT jwt = JWT.decode(realToken);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            log.debug("jwtUtil---verify token解码错误:{}",e.getMessage());
            return null;
        }
    }

    /**
     * 生成签名 signature
     *
     * @param username
     * @param secret
     * @return 加密后的 token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);
    }
}