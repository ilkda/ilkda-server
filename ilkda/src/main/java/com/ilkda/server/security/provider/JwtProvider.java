package com.ilkda.server.security.provider;

import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.security.details.CustomUserDetails;
import com.ilkda.server.security.details.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtProvider implements AuthenticationProvider {

    // 토큰 유효기간
    private static final long ACCESS_TOKEN_DURATION = 1000L * 60 * 120;
    private static final long REFRESH_TOKEN_DURATION = 1000L * 3600 * 480;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${group.name}")
    private String ISSUER;

    private final CustomUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    private String generate(long expireTime, Claims claims) {
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
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

    public String extractToken(HttpServletRequest req) {
        String authHeader = Optional.ofNullable(req.getHeader("Authorization"))
                .orElseThrow(() -> new UnauthorizedException("헤더에 토큰이 없습니다."));
        if (!authHeader.startsWith("Bearer ")) throw new UnauthorizedException("헤더에 올바른 토큰이 없습니다.");
        return authHeader.replace("Bearer ", "");
    }

    public void validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            if(claims.getExpiration().before(new Date(System.currentTimeMillis()))) throw new UnauthorizedException("토큰이 유효하지 않습니다.");
        } catch (Exception e) {
            throw new UnauthorizedException("토큰 파싱 실패");
        }
    }

    public Long getMemberIdFromToken(String accessToken) {
        return ((Integer) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody().get("member_id")).longValue();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(Long.toString((Long)authentication.getPrincipal()));

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}