package com.just.book_appoint_system.util;


import com.just.book_appoint_system.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt工具类
 */
@Component
public class JwtUtil {
    public static final String SUBJECT="book";
    public static final long Ex_Time=60*1000*60*24;//过期时间，毫秒
    public static final String APPSECRET="JUST@13999";//密钥

    /**
     * 加密token
     * @param user
     * @return
     */
    public static String creatJwt(User user){

        if(user==null){
            return "";
        }
        String token= Jwts.builder().setSubject(SUBJECT)
                .claim("permission",user.getPermission())
                .claim("sid",user.getSchoolNumber())
                .claim("name", user.getName())
                .setIssuedAt(new Date())
                .setNotBefore(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+Ex_Time))
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();
        return token;
    }

    /**
     * 解密token
     * @param token
     * @return
     */
    public static Claims checkJWT(String token){
        try {
            final Claims claims= Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
            return claims;
        }catch (Exception e){
            return null;
        }

    }
}
