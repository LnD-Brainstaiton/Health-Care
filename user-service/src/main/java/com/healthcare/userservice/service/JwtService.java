package com.healthcare.userservice.service;

import com.healthcare.userservice.common.utils.DateTimeUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService extends BaseService{

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public String   generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        claims.put("userIdentity", userName);
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public static String generateToken(Map<String, Object> claims,
                                       String userName,
                                       String expiryTime,
                                       String jwtSecretKey) {
        return createToken(claims, userName, expiryTime, jwtSecretKey);
    }

    private static String createToken(Map<String, Object> claims,
                                      String subject,
                                      String expiryTime,
                                      String jwtSecretKey) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getExpiryMilli(expiryTime)))
                .signWith(getSignInKey(jwtSecretKey), SignatureAlgorithm.HS512)
                .compact();
    }

    private static int getExpiryMilli(String expiryTime) {
        int expMinutes = Integer.parseInt(expiryTime);
        return DateTimeUtils.convertToMilli(expMinutes, Calendar.MINUTE);
    }

    private static Key getSignInKey(String jwtSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
