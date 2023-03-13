package com.ilkda.server.auth.service;

import com.ilkda.server.auth.JwtGenerator;
import com.ilkda.server.auth.dto.KakaoUserInfo;
import com.ilkda.server.auth.dto.TokenDTO;
import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.member.service.MemberService;
import com.ilkda.server.utils.jwt.JwtUtil;
import com.ilkda.server.utils.jwt.JwtPayload;
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

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtGenerator jwtGenerator;

    public TokenDTO registerUser(String kakaoToken) {
        Long memberId = memberService.createUser(getKakaoUserInfo(kakaoToken));

        String accessToken = jwtGenerator.generateAccessToken(
                JwtPayload.builder()
                        .member_id(memberId)
                        .type(JwtPayload.JwtType.ACCESS)
                        .build()
        );

        String refreshToken = jwtGenerator.generateRefreshToken(
                JwtPayload.builder()
                        .member_id(memberId)
                        .type(JwtPayload.JwtType.REFRESH)
                        .build()
        );

        return new TokenDTO(accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken) {
        JwtUtil jwtUtil = new JwtUtil(refreshToken);

        String accessToken = jwtGenerator.refreshAccessToken(jwtUtil);
        refreshToken = jwtGenerator.refreshRefreshToken(jwtUtil);

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


