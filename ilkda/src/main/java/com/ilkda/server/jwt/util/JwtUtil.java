package com.ilkda.server.jwt.util;

import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.jwt.payload.JwtPayload;

public interface JwtUtil {

    String getToken();

    JwtPayload getPayload();

    default void validateExpiration(long expiration) {
        if(expiration < System.currentTimeMillis()) {
            throw new UnauthorizedException("토큰이 유효하지 않습니다.");
        }
    }

    JwtPayload parsePayload();
}
