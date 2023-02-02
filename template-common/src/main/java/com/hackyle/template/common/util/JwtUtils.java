package com.hackyle.template.common.util;


import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hackyle.template.common.pojo.JwtPayload;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    /** JWT失效时间(单位：ms) */
    private static final long EXPIRE_TIME = 7200000; //2h = 1000 * 60 * 60 * 2
    /** 如果当前JWT的失效时间只有EXPIRE_REFRESH，则签发新的Token(单位：ms) */
    private static final long EXPIRE_REFRESH = 900000; //15min = 1000 *  60 * 15

    /** JWT的header内容 */
    private static final Map<String, Object> jwtHeader = new HashMap<>();


    /** JWT加密秘钥 */
    private static final String TOKEN_SECRET = "blog.hackyle.com:default.token.secret";

    static {
        jwtHeader.put("alg", "HS256"); //alg属性表示签名的算法（algorithm），默认是 HMAC SHA256（写成 HS256）
        jwtHeader.put("typ", "JWT"); //typ属性表示这个令牌（token）的类型（type），JWT 令牌统一写为JWT
    }


    /**
     * 签发Token：依次构造JWT的三大组成部分：Header，Payload，Signature，调用API生成Token
     */
    public static String createJWT(JwtPayload jwtPayload) {
        Date now = new Date();//当前时间

        JWTCreator.Builder builder = JWT.create();
        //JWT的Token体第一部分: Header
        builder.withHeader(jwtHeader);

        //JWT的Token体第二部分: Payload
        builder.withKeyId(jwtPayload.getId()); //编号
        builder.withSubject(jwtPayload.getSubject()); //主题
        builder.withAudience(jwtPayload.getAudience()); //受众
        builder.withIssuer(jwtPayload.getIssuer()); //签发人
        builder.withIssuedAt(now); //签发时间
        builder.withExpiresAt(new Date(now.getTime() + EXPIRE_TIME)); //过期时间
        //payload中的其他数据
        Map<String, Object> payloadExtendMap = jwtPayload.getPayloadExtendMap();
        if(payloadExtendMap != null && !payloadExtendMap.isEmpty()) {
            builder.withClaim("payloads", payloadExtendMap);
        }

        ////JWT的Token体第三部分: Signature
        return builder.sign(Algorithm.HMAC512(TOKEN_SECRET));
    }

    /**
     * 校验Token：抛出TokenExpiredException 则说明已过期
     */
    public static boolean validateJWT(String token) {
        if(token == null || "".equals(token.trim())) {
            throw new IllegalArgumentException("解析Token时入参为空");
        }
        boolean result = true; //Token解析状态

        try {
            JWT.require(Algorithm.HMAC512(TOKEN_SECRET))
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e){
            result = false;
        }

        return result;
    }


    /**
     * 检查是否Token是否已经临近失效
     * 如果是则更新Token，否则返回原Token
     */
    public static String refreshCheck(String oldToken) {
        String newToken = oldToken;
        Date expiration = null;

        //Token是有效的 && 已经达到设置中的过期时间
        if(validateJWT(oldToken)) {
            Date now = new Date();
            DecodedJWT verify = JWT.require(Algorithm.HMAC512(TOKEN_SECRET)).build().verify(oldToken);
            expiration = verify.getExpiresAt();

            //已经符合过期时间的设定了
            if(expiration.getTime() - now.getTime() < EXPIRE_REFRESH) {
                JWTCreator.Builder builder = JWT.create();

                builder.withHeader(jwtHeader);
                builder.withJWTId(verify.getKeyId());
                builder.withSubject(verify.getSubject());
                builder.withAudience(String.valueOf(verify.getAudience()));
                builder.withIssuer(verify.getIssuer());

                //重新设置签发时间和过期时间
                builder.withIssuedAt(now);
                builder.withExpiresAt(new Date(now.getTime() + EXPIRE_TIME));

                builder.withClaim("payloads", verify.getPayload());

                //在原来Token的基础上，重新设置了签发时间和过期时间后，重新生成Token
                newToken = builder.sign(Algorithm.HMAC512(TOKEN_SECRET));
            }
        }

        return  newToken;
    }

    /**
     * 获取Token中的数据
     */
    public static JwtPayload parseJWT(String token) {
        if(!validateJWT(token)) {
            throw new IllegalArgumentException("Token已过期");
        }

        DecodedJWT decodedJWT = JWT.decode(token);
        String keyId = decodedJWT.getKeyId();
        String subject = decodedJWT.getSubject();
        String issuer = decodedJWT.getIssuer();

        return new JwtPayload(keyId, subject, issuer);
    }


    public static void main(String[] args) {
        String token = "eyJraWQiOm51bGwsInR5cCI6IkpXVCIsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJhZG1pbiIsImlzcyI6ImFkbWluIiwiZXhwIjoxNjY2MTU0NzkwLCJpYXQiOjE2NjYxNDc1OTB9.HSAi-2k1ikZr6qJDnCHESxC89y_mj1cOIqle_OS83CU1u6SAwCImgMyCbYQ_F-UK_5vcrcjvGrFymLaMaQRUVQ";
        JwtPayload jwtPayload1 = parseJWT(token);
        System.out.println(JSON.toJSONString(jwtPayload1));


        //JwtPayload jwtPayload = new JwtPayload("0001", "subject01", "issuer01");
        //String token = createJWT(jwtPayload);
        //System.out.println(token);
        //
        //JwtPayload jwtPayload1 = parseJWT(token);
        //System.out.println(JSON.toJSONString(jwtPayload1));
        //
        //if(validateJWT(token)) {
        //    DecodedJWT verify = JWT.require(Algorithm.HMAC512(TOKEN_SECRET)).build().verify(token);
        //    System.out.println(verify.getKeyId());
        //    System.out.println(verify.getSubject());
        //}
        //
        //System.out.println(refreshCheck(token));
    }
}

