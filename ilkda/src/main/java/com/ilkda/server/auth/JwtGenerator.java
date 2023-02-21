package com.ilkda.server.auth;

import com.ilkda.server.config.MemberTokenConfig;
import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.utils.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtGenerator {

    private static final long ACCESS_TOKEN_DURATION = 1000L * 60 * 120;
    private static final long REFRESH_TOKEN_DURATION = 1000L * 3600 * 480;

    private static String doGenerate(long expireTime, Claims claims) {
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, MemberTokenConfig.getSecret())
                .compact();
    }

    public static String generateAccessToken(Map<String, Object> fields) {
        Claims claims = Jwts.claims();
        claims.put("type", "access");
        for (String key : fields.keySet()) {
            claims.put(key, fields.get(key));
        }
        return doGenerate(ACCESS_TOKEN_DURATION, claims);
    }

    public static String generateRefreshToken(Map<String, Object> fields) {
        Claims claims = Jwts.claims();
        claims.put("type", "refresh");
        for (String key : fields.keySet()) {
            claims.put(key, fields.get(key));
        }
        return doGenerate(REFRESH_TOKEN_DURATION, claims);
    }

    public static String refreshAccessToken(JwtUtil jwtUtil) {
        if (!jwtUtil.getFieldFromToken("type", String.class).equals("refresh")) {
            throw new UnauthorizedException("ACCESS 토큰을 갱신할 수 없습니다.");
        }

        Map<String, Object> fields = new HashMap<>();
        jwtUtil.getRequiredFields()
                .forEach(field ->
                        fields.put(field, jwtUtil.getFieldFromToken(field, Object.class)));
        return generateAccessToken(fields);
    }

    public static String refreshRefreshToken(JwtUtil jwtUtil) {
        String refreshToken = jwtUtil.getToken();
        if (!jwtUtil.checkRefreshToken()) {
            Map<String, Object> fields = new HashMap<>();
            jwtUtil.getRequiredFields().forEach(field -> fields.put(field, jwtUtil.getFieldFromToken(field, Object.class)));
            refreshToken = generateRefreshToken(fields);
        }
        return refreshToken;
    }
}
