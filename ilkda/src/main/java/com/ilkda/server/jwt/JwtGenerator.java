package com.ilkda.server.jwt;

import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.jwt.payload.MemberJwtPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtGenerator {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private String doGenerate(MemberJwtPayload payload) throws IllegalAccessException {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        Claims claims = Jwts.claims(payload.getPayloads());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateAccessToken(MemberJwtPayload payload) {
        try {
            return doGenerate(payload);
        } catch (IllegalAccessException e) {
            throw new UnauthorizedException("ACCESS 토큰 발급 실패");
        }
    }

    public String generateRefreshToken(MemberJwtPayload payload) {
        try {
            return doGenerate(payload);
        } catch (IllegalAccessException e) {
            throw new UnauthorizedException("REFRESH 토큰 발급 실패");
        }
    }
}
