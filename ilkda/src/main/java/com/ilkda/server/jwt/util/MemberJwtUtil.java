package com.ilkda.server.jwt.util;

import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.jwt.payload.JwtPayload;
import com.ilkda.server.jwt.payload.MemberJwtPayload;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class MemberJwtUtil extends JwtUtil {

    public MemberJwtUtil(String token) {
        super(token);
    }

    /**
     * jwt의 {헤더.페이로드.서명} 형식에서 페이로드를 가져오기 위해 [.]을 기준으로 split 후 두 번째 인자를 사용합니다. <br/>
     * jwt는 HS256 (Base64(Header) + Base64(Payload) + secret key) 알고리즘으로 인코딩 되었으므로 Base64로 페이로드를 디코딩 합니다.
     * */
    @Override
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

