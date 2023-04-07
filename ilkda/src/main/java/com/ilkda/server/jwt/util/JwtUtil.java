package com.ilkda.server.jwt.util;

import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.jwt.payload.JwtPayload;

public abstract class JwtUtil {

    private final String token;
    private final JwtPayload payload;

    public JwtUtil(String token) {
        this.token = token;
        this.payload = parsePayload();
        validateToken();
    }

    public String getToken() {
        return this.token;
    }

    public JwtPayload getPayload() {
        return this.payload;
    }

    abstract JwtPayload parsePayload();

    private void validateToken() {
        if(payload.getExp() < System.currentTimeMillis()) {
            throw new UnauthorizedException("토큰이 유효하지 않습니다.");
        }
    }
}