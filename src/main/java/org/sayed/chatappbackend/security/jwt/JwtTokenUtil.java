package org.sayed.chatappbackend.security.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
public class JwtTokenUtil implements Serializable {


    private static final int TOKEN_VALIDATION = 1 * 60 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;


// create jwt
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), userDetails.getAuthorities());
    }
    private String doGenerateToken(Map<String, Object> claims, String username, Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder().setClaims(claims)
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDATION * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    // validate jwt
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUsernameFromToken(token);
        Collection<? extends GrantedAuthority> authorities = getAuthoritiesFromToken(token);
        return username.equals(userDetails.getUsername()) &&

                !isTokenExpired(token);
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token){
        return (Collection<? extends GrantedAuthority>) Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("authorities");
    }

    private boolean isTokenExpired(String token) {
        Date date = getExpirationDate(token);
        return date.before(new Date());
    }

    private Date getExpirationDate(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
    }









}
