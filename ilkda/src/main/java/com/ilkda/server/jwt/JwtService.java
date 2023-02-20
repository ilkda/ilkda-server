package com.ilkda.server.jwt;

import com.ilkda.server.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtService {

    // 토큰 유효기간
    private static final long ACCESS_TOKEN_DURATION = 1000L * 60 * 120;
    private static final long REFRESH_TOKEN_DURATION = 1000L * 3600 * 480;

    @Value("${jwt.secret}")
    private String SECRET;
    private static String SECRET_KEY;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET.getBytes());
    }

    private static String generate(long expireTime, Claims claims) {
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static String generateAccessToken(String kakaoToken, Long memberId) {
        Claims claims = Jwts.claims().setSubject(String.format("member%d", memberId));
        claims.put("kakao_token", kakaoToken);
        claims.put("member_id", memberId);
        return generate(ACCESS_TOKEN_DURATION, claims);
    }

    public static String generateRefreshToken(String kakaoToken, Long memberId) {
        Claims claims = Jwts.claims();
        claims.put("kakao_token", kakaoToken);
        claims.put("member_id", memberId);
        return generate(REFRESH_TOKEN_DURATION, claims);
    }

    public static void validateToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new UnauthorizedException("토큰 파싱 실패");
        }
        if(claims.getExpiration().before(new Date(System.currentTimeMillis()))) throw new UnauthorizedException("토큰이 유효하지 않습니다.");
    }

    public static Long getMemberIdFromToken(String accessToken) {
        return ((Integer) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody().get("member_id")).longValue();
    }
}
