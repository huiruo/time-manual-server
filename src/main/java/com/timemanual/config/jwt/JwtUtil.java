package com.timemanual.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtil {
    /**
     * 过期时间30分钟
     */
//    public static final long EXPIRE_TIME = 30 * 60 * 1000;
//    public static final long EXPIRE_TIME = 30 * 60 * 1000;
    private static final long EXPIRE_TIME = 60 * 1000;


    /**
     * 校验token是否正确
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            log.debug("jwtUtil---verify success:{}");
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

    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            log.error("error：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 生成签名,5min(分钟)后过期
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }
}
