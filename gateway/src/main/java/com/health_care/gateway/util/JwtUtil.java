package com.health_care.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public class JwtUtil {

    private JwtUtil() {}

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    public static void validateToken(String token, String jwtSecretKey) {
        if (isTokenExpired(token, jwtSecretKey)) {
            throw new ExpiredJwtException(null, null, "Token has expired. Please try again");
        }
    }

    private static boolean isTokenExpired(String token, String jwtSecretKey) {
        try {
            return extractExpiration(token, jwtSecretKey).before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static Date extractExpiration(String token, String jwtSecretKey) {
        return extractClaim(token, jwtSecretKey, Claims::getExpiration);
    }

    public static <T> T extractClaimByKey(String token, String jwtSecretKey, String key, Class<T> classType) {
        return extractClaim(token, jwtSecretKey, claims -> claims.get(key, classType));
    }

    private static <T> T extractClaim(String token,
                                      String jwtSecretKey,
                                      Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, jwtSecretKey);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token, String jwtSecretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey(jwtSecretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getSignInKey(String jwtSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
