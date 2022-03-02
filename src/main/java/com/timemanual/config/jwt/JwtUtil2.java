package com.timemanual.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil2 {
    public static final long EXPIRE_TIME = 5*60*1000;//token到期时间5分钟，毫秒为单位
    public static final long REFRESH_EXPIRE_TIME = 30*60;//RefreshToken到期时间为30分钟，秒为单位
    private static final String TOKEN_SECRET = "ljdyaishijin**3nkjnj??";  //密钥盐

    private static final String CLAIM_NAME = "username";
    // private static final String CLAIM_NAME="account";



    /**
     * @Description  ：生成token
     * @author       : lj
     * @param        : [user]
     * @return       : java.lang.String
     * @exception    :
     * @date         : 2020-1-31 22:49
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
     * @Description  ：token验证
     * @author       : lj
     * @param        : [token]
     * @return       : java.lang.Boolean
     * @exception    :
     * @date         : 2020-1-31 22:59
     */
    public static Boolean verify(String token) throws Exception{

        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();//创建token验证器
        DecodedJWT decodedJWT=jwtVerifier.verify(token);
        System.out.println("认证通过：");
        System.out.println(CLAIM_NAME + decodedJWT.getClaim(CLAIM_NAME).asString());
        System.out.println("过期时间：      " + decodedJWT.getExpiresAt());
        return true;
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
