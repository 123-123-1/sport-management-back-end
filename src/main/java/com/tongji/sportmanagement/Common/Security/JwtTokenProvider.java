package com.tongji.sportmanagement.Common.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private String secretKey = "TongjiSoftwareEngineeringProjectSportsManagementSystemJoySports";

    private long jwtExpirationInMs = 1000 * 3600 * 3; // 3小时

    // 生成JWT令牌
    public String generateToken(int id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .claim("id", id)
                .expiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))  // 使用256位密钥进行签名
                .compact();
    }

    public Instant getExpiryDate() {
        Date now = new Date();
        return new Date(now.getTime() + jwtExpirationInMs).toInstant();
    }

    // 从JWT令牌中获取用户ID
    public int tryGetIdFromToken(String token) throws JwtException {
        var parser = Jwts.parser()  // 修正parser，使用新版本的JJWT API
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))  // 使用安全密钥
                .build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.get("id", Integer.class);
    }

    // 从JWT令牌中获取过期时间
    public Date tryGetExpirationDateFromToken(String token) throws JwtException {
        var parser = Jwts.parser()  // 修正parser，使用新版本的JJWT API
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))  // 使用安全密钥
                .build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }

    // 生成永久有效的JWT令牌（没有过期时间）
    public String generatePermanentToken(int id) {
        return Jwts.builder()
                .claim("id", id)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))  // 使用256位密钥进行签名
                .compact();
    }
}
