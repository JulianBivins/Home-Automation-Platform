package com.homeautomation.homeAutomation.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class JwtServiceConfig {

    @Value("${jwt.secret.key}")
    private String secretKeyString;

    private Key secretKey;

    private Key getSignInKey() {
        if (this.secretKey == null) {
            byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return this.secretKey;
    }

//    private Key getSignInKey() {
//        return Keys.secretKeyFor(SignatureAlgorithm.HS256); // HS256 ensures a secure key size
//    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim (String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken (
            UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken (
            Map<String, Object> extraClaims, //Any extra information
            UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date (System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Use HS256 for HMAC
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims (String token){
        return Jwts.
                parserBuilder()
                .setSigningKey(getSignInKey()) //Sign in Key is the secret signature to verify the sender is correct and the message hasn't been tempered with
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
