package com.beyond.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JWTUtil {
    // 过期时间8小时
    private static final long EXPIRE_TIME = 8 * 60 * 60 * 1000;
    private static final String SECRET = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String account) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("account", account).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的信息
     */
    public static Integer getId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asInt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("name").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("account").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param account 用户名
     * @return 加密的token
     */
    public static String sign(Integer id, String account, String name) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            // 附带username信息
            return JWT.create().withClaim("id", id).withClaim("account", account).withClaim("name", name)
                    .withExpiresAt(date).sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成签名,指定时间过期
     *
     * @param account 用户名
     * @return 加密的token
     */
    public static String sign(Integer id, String account, String name, Long time) {
        try {
            Date date = new Date(System.currentTimeMillis() + time);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            // 附带username信息
            return JWT.create().withClaim("id", id).withClaim("account", account).withClaim("name", name)
                    .withExpiresAt(date).sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }
}
