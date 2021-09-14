package com.dnliu.pdms.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dnliu.pdms.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt工具类，生成JWT和认证
 * @author dnliu
 * @date 2021-09-11 20:46
 */
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    /**
     * 密钥
     */
    private static final String SECRET = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiYWRtaW4iLCJpZCI6MywidXNlck5hbWUiOiJhZG1pbiIsImV4cCI6MTYzMTM2OTY3MSwiaWF0IjoxNjMxMzY5NjYxfQ.KO_NiY0lj2L-oXG_DYfxkHFq6XOSZPHd2yu92DKRnhI";

    /**
     * 过期时间
     **/
    private static final long EXPIRATION = 600L;//单位为秒

    /**
     * 生成用户token,设置token超时时间
     */
    public static String createToken(User user) {
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("id", user.getId())//userId
                .withClaim("userName", user.getUserName())//userName
                .withClaim("name", user.getUserName())//name
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(new Date()) //签发时间
                .sign(Algorithm.HMAC256(SECRET)); //SECRET加密
        return token;
    }

    /**
     * 校验token并解析token
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            if (StringUtil.isNotBlank(e.getMessage()) && e.getMessage().indexOf("The Token has expired on") >= 0) {
                logger.error("token已过期, token: {}", token);
                //解码异常则抛出异常
                return null;
            }

            logger.error(e.getMessage());
            logger.error("token解码异常, e: ", e);
            //解码异常则抛出异常
            return null;
        }

        return jwt.getClaims();
    }

}
