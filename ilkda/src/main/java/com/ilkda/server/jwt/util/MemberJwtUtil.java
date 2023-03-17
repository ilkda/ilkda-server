package com.ilkda.server.jwt.util;

import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.jwt.payload.MemberJwtPayload;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class MemberJwtUtil extends JwtUtil {

    public MemberJwtUtil(String token) {
        super(token);
    }

     public MemberJwtPayload parsePayload() {
        try {
            JSONParser jsonParser = new JSONParser();
            String jsonString = new String(Base64.getUrlDecoder().decode(this.getToken().split("\\.")[1]));
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            return MemberJwtPayload.of(jsonObject);
        } catch (ParseException e) {
            throw new UnauthorizedException("JWT 파싱 실패");
        }
    }

    public boolean checkRefreshToken(long duration) {
        return System.currentTimeMillis() - getPayload().getExp() <= duration/2;
    }
}

