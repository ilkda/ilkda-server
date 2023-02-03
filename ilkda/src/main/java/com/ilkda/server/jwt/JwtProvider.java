package com.ilkda.server.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    // 토큰 유효기간
    private static final long ACCESS_TOKEN_DURATION = 1000L * 60 * 120;
    private static final long REFRESH_TOKEN_DURATION = 1000L * 3600 * 480;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${group.name}")
    private String ISSUER;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    private String generate(long expireTime, Claims claims) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime))
                .setClaims(claims)
                .setIssuer(ISSUER)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateAccessToken(String kakaoToken, Long memberId) {
        Claims claims = Jwts.claims().setSubject(String.format("member%d", memberId));
        claims.put("kakao_token", kakaoToken);
        claims.put("member_id", memberId);
        return generate(ACCESS_TOKEN_DURATION, claims);
    }

    public String generateRefreshToken(String kakaoToken, Long memberId) {
        Claims claims = Jwts.claims();
        claims.put("kakao_token", kakaoToken);
        claims.put("member_id", memberId);
        return generate(REFRESH_TOKEN_DURATION, claims);
    }

}
