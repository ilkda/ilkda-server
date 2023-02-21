package com.ilkda.server.utils.jwt;

import com.ilkda.server.config.MemberTokenConfig;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.*;

public abstract class JwtUtil {

    // 토큰 유효기간
    private static final long REFRESH_TOKEN_DURATION = 1000L * 3600 * 480;

    private final String SECRET_KEY;

    private final String token;
    private final Set<String> requiredFields;

    public JwtUtil(String token, String secretKey) {
        this(token, null, secretKey);
    }

    public JwtUtil(String token, Set<String> requiredFields, String secretKey) {
        this.token = token;
        this.requiredFields = requiredFields;
        this.SECRET_KEY = secretKey;
        validateToken();
    }

    public String getToken() {
        return this.token;
    }

    public Set<String> getRequiredFields() {
        return this.requiredFields;
    }

    private void validateToken() {
        if(getClaimsFromToken()
                .getExpiration()
                .before(new Date(System.currentTimeMillis())))
            throw new UnauthorizedException("토큰이 유효하지 않습니다.");
    }

    private Claims getClaimsFromToken() {
        try {
             return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new UnauthorizedException("토큰 파싱 실패");
        }
    }

    public <T> T getFieldFromToken(String name, Class<T> returnType) {
        if(!requiredFields.contains(name)) throw new UnauthorizedException("해당 필드가 존재하지 않습니다.");
        return returnType.cast(getClaimsFromToken().get(name));
    }

    public Date getExpirationFromToken() {
        return getClaimsFromToken().getExpiration();
    }

    public boolean checkRefreshToken() {
        return System.currentTimeMillis() - getExpirationFromToken().getTime() <= REFRESH_TOKEN_DURATION/2;
    }
}

