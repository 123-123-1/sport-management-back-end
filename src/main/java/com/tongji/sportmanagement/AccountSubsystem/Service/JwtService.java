package com.tongji.sportmanagement.AccountSubsystem.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    public final static long EXPIRE_TIME=30*60*1000; //30分钟

    public static String getToken(int id) {
        Date date=new Date(System.currentTimeMillis()+EXPIRE_TIME);

        Map<String,Object> map=new HashMap<>();
        return JWT.create().withHeader(map)
                .withClaim("userId", id)
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256("joy_sports"));
    }

    public static boolean verify(String token){
        try {
            JWT.require(Algorithm.HMAC256("1234")).build().verify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static int getUserId(String token){
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaim("userId").asInt();
    }

}
