package com.ilkda.server.utils.jwt;

import com.ilkda.server.exception.UnauthorizedException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class JwtUtil {

    // 토큰 유효기간
    private static final long REFRESH_TOKEN_DURATION = 1000L * 3600 * 480;

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

    private void validateToken() {
        if(payload.getExp() < System.currentTimeMillis()) {
            throw new UnauthorizedException("토큰이 유효하지 않습니다.");
        }
    }

    private JwtPayload parsePayload() {
        try {
            JSONParser jsonParser = new JSONParser();
            String jsonString = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            return JwtPayload.of(jsonObject);
        } catch (ParseException e) {
            throw new UnauthorizedException("JWT 파싱 실패");
        }
    }

    public boolean checkRefreshToken() {
        return System.currentTimeMillis() - payload.getExp() <= REFRESH_TOKEN_DURATION/2;
    }
}

