package com.ilkda.server.auth.service;

import com.ilkda.server.auth.JwtGenerator;
import com.ilkda.server.auth.dto.KakaoUserInfo;
import com.ilkda.server.auth.dto.TokenDTO;
import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.member.service.MemberService;
import com.ilkda.server.utils.jwt.JwtUtil;
import com.ilkda.server.utils.jwt.MemberJwtUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberService memberService;
    private static final String TOKEN_FIELD_MEMBER_ID = "member_id";

    public TokenDTO registerUser(String kakaoToken) {
        // 1. Access token으로 카카오에서 사용자 정보 가져오기
        // 2. 사용자 정보 저장하기
        String memberId = Long.toString(memberService.createUser(getKakaoUserInfo(kakaoToken)));

        // 3. Access token, Refresh token 발급하기
        Map<String, Object> accessTokenClaims = new HashMap<>();
        accessTokenClaims.put(TOKEN_FIELD_MEMBER_ID, memberId);
        String accessToken = JwtGenerator.generateAccessToken(accessTokenClaims);

        Map<String, Object> refreshTokenClaims = new HashMap<>();
        refreshTokenClaims.put(TOKEN_FIELD_MEMBER_ID, memberId);
        String refreshToken = JwtGenerator.generateRefreshToken(refreshTokenClaims);

        return new TokenDTO(accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken) {
        JwtUtil jwtUtil = new MemberJwtUtil(refreshToken);

        String accessToken = JwtGenerator.refreshAccessToken(jwtUtil);
        refreshToken = JwtGenerator.refreshRefreshToken(jwtUtil);

        return new TokenDTO(accessToken, refreshToken);
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken)  {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s", accessToken));
        HttpEntity request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    request,
                    String.class);
        } catch (Exception e) {
            throw new UnauthorizedException("해당 토큰의 사용자 정보를 조회할 수 없습니다.");
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(response.getBody());
        } catch (ParseException p) {
            throw new UnauthorizedException("토큰 파싱 실패");
        }
        return new KakaoUserInfo(jsonObject);
    }
}


