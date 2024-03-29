package com.ryulth.sns.account.service.user;

import com.ryulth.sns.account.dto.TokenDto;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService implements TokenService {
    private @Value("${jwt.secret.access-token}")
    String accessSecretKey;
    private @Value("${jwt.secret.refresh-token}")
    String refreshSecretKey;
    private @Value("${jwt.expiration-time.access-token}")
    int accessTokenExpirationTime;
    private @Value("${jwt.expiration-time.refresh-token}")
    int refreshTokenExpirationTime;


    @Override
    public <T> TokenDto publishToken(Map<String, Object> body, T subject) {
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject.toString())
                .setClaims(body)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * accessTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, this.generateKey(accessSecretKey))
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject.toString())
                .setClaims(body)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * refreshTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, this.generateKey(refreshSecretKey))
                .compact();
        return TokenDto.builder().tokenType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public String getEmailFromRefreshToken(String token) throws IllegalAccessException {
        return getEmailFromToken(token,refreshSecretKey);
    }

    @Override
    public String getEmailFromAccessToken(String token) throws IllegalAccessException {
        String tokenBody = token.replace("bearer ","");
        return getEmailFromToken(tokenBody,accessSecretKey);
    }
    private String getEmailFromToken(String token,String secretKey) throws IllegalAccessException {
        try {
            Claims claims = Jwts.parser().setSigningKey(this.generateKey(secretKey))
                    .parseClaimsJws(token).getBody(); // 정상 수행된다면 해당 토큰은 정상토큰
            return claims.get("email").toString();
        } catch (JwtException e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }
    private byte[] generateKey(String secretKey) {
        byte[] key = null;
        try {
            key = secretKey.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return key;
    }

    @Override
    public boolean isUsable(String jwt) {
        try{
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(this.generateKey(accessSecretKey))
                    .parseClaimsJws(jwt);
            return true;
        }catch (Exception e) {
            throw e;
        }
    }

}
