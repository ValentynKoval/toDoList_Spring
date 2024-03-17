package org.spring.todo.todolist.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.spring.todo.todolist.models.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private Duration accessTokenLifetime;

    @Value("${security.jwt.refresh-token.expiration}")
    private Duration refreshTokenLifetime;

    private final TokenService tokenService;
    private final UserService userService;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);

        Date issuedDate = new Date();
        Date expiredEccessTokenDate = getExpiredEccessToken(issuedDate);
        Date expiredRefreshTokenDate = getExpiredRefreshToken(issuedDate);

        String username = userDetails.getUsername();

        Token refreshtoken = tokenService.saveNewToken(tokenBuilder(claims, username, issuedDate, expiredRefreshTokenDate));
        userService.setToken(username, refreshtoken);

        return tokenBuilder(claims, username, issuedDate, expiredEccessTokenDate);
    }

    public Date getExpiredEccessToken(Date issuedDate) {
        return new Date(issuedDate.getTime() + accessTokenLifetime.toMillis());
    }

    public Date getExpiredRefreshToken(Date issuedDate) {
        return new Date(issuedDate.getTime() + refreshTokenLifetime.toMillis());
    }

    public String tokenBuilder(Map<String, Object> claims, String username, Date issuedDate, Date expiredDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String refreshTokensByClaims(List<String> rolesList, String username) {
        Date issuedDate = new Date();
        Date expiredEccessTokenDate = getExpiredEccessToken(issuedDate);
        Date expiredRefreshTokenDate = getExpiredRefreshToken(issuedDate);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", rolesList);

        Token refreshtoken = tokenService.saveNewToken(tokenBuilder(claims, username, issuedDate, expiredRefreshTokenDate));
        userService.setToken(username, refreshtoken);

        return tokenBuilder(claims, username, issuedDate, expiredEccessTokenDate);
    }
}
