package com.timemanual.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil2 {
    /**
     * 过期时间 1 小时
     */
    private static final long EXPIRE_TIME = 60 * 60 * 1000;
    /**
     * 密钥
     */
    private static final String SECRET = "price";

    /**
     * 生成 token
     */
    public static String createToken(String sessionId) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        // 附带sessionId信息
        return JWT.create()
                .withClaim("sessionId", sessionId)
                //到期时间
                .withExpiresAt(date)
                //创建一个新的JWT，并使用给定的算法进行标记
                .sign(algorithm);
    }

    /**
     * 校验 token 是否正确
     */
    public static boolean verify(String token, String sessionId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //在token中附带了sessionId信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("sessionId", sessionId)
                    .build();
            //验证 token
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     */
    public static String getSessionId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("sessionId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
