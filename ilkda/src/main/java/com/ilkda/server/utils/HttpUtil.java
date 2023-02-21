package com.ilkda.server.utils;

import com.ilkda.server.exception.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class HttpUtil {

    public static String getTokenFromRequest(HttpServletRequest req) {
        String authHeader = Optional.ofNullable(req.getHeader("Authorization"))
                .orElseThrow(() -> new UnauthorizedException("헤더에 토큰이 없습니다."));
        if (!authHeader.startsWith("Bearer ")) throw new UnauthorizedException("헤더에 올바른 토큰이 없습니다.");
        return authHeader.replace("Bearer ", "");
    }

    public static <T> T getAttributeFromRequest(HttpServletRequest req, String name, Class<T> responseType) {
        return responseType.cast(req.getAttribute(name));
    }
}
