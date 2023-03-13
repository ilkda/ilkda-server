package com.ilkda.server.auth;

import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.utils.jwt.JwtUtil;
import com.ilkda.server.utils.jwt.JwtPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtGenerator {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static final long ACCESS_TOKEN_DURATION = 1000 * 60 * 120;
    private static final long REFRESH_TOKEN_DURATION = 1000 * 3600 * 480;

    private String doGenerate(JwtPayload payload) throws IllegalAccessException {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        Claims claims = Jwts.claims();
        claims.putAll(payload.getPayloadMap());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateAccessToken(JwtPayload payload) {
        try {
            payload.setExp(System.currentTimeMillis() + JwtGenerator.ACCESS_TOKEN_DURATION);
            return doGenerate(payload);
        } catch (IllegalAccessException e) {
            throw new UnauthorizedException("ACCESS 토큰 생성 실패");
        }
    }

    public String generateRefreshToken(JwtPayload payload) {
        try {
            payload.setExp(System.currentTimeMillis() + JwtGenerator.REFRESH_TOKEN_DURATION);
            return doGenerate(payload);
        } catch (IllegalAccessException e) {
            throw new UnauthorizedException("REFRESH 토큰 생성 실패");
        }
    }

    public String refreshAccessToken(JwtUtil jwtUtil) {
        if (!jwtUtil.getPayload().getType().equals(JwtPayload.JwtType.REFRESH)) {
            throw new UnauthorizedException("ACCESS 토큰을 갱신할 수 없습니다.");
        }

        return generateAccessToken(jwtUtil.getPayload());
    }

    public String refreshRefreshToken(JwtUtil jwtUtil) {
        String refreshToken = jwtUtil.getToken();
        if (!jwtUtil.checkRefreshToken()) {
            refreshToken = generateRefreshToken(jwtUtil.getPayload());
        }
        return refreshToken;
    }
}
