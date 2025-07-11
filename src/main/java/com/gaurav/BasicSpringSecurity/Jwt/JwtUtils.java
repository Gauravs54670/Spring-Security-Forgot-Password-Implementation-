package com.gaurav.BasicSpringSecurity.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {

    //getting Key for sign in
    public SecretKey getSignInKey() {
        String secretKey ="my-super-secret-key-that-is-long-enough-1234567890!~@#$";
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    //generating token
    public String generatingToken(UserDetails userDetails) {
        // one hour time
        long expirationTime = 1000 * 60 * 60;
        Map<String, Object> claims = new HashMap<>();
        claims.put("username",userDetails.getUsername());
        claims.put("role",userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        System.out.print(claims);
        return Jwts.builder()
                .signWith(getSignInKey())
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();
    }
    //extracting all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    //extract username
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    //check token expiration
    private Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
    //validate the token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
