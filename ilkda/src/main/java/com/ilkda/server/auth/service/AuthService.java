package com.ilkda.server.auth.service;

import com.ilkda.server.jwt.JwtGenerator;
import com.ilkda.server.auth.dto.KakaoUserInfo;
import com.ilkda.server.auth.dto.TokenDTO;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.member.service.MemberService;
import com.ilkda.server.jwt.util.MemberJwtUtil;
import com.ilkda.server.jwt.payload.JwtPayload;
import com.ilkda.server.jwt.payload.MemberJwtPayload;
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

    private final static long ACCESS_TOKEN_DURATION = 1000 * 60 * 120;
    private final static long REFRESH_TOKEN_DURATION = 1000 * 3600 * 480;

    public TokenDTO login(String kakaoToken) {
        Long memberId = memberService.createUser(getKakaoUserInfo(kakaoToken));

        String accessToken = jwtGenerator.generateAccessToken(
                MemberJwtPayload.builder()
                        .type(JwtPayload.JwtType.ACCESS)
                        .exp(System.currentTimeMillis() + ACCESS_TOKEN_DURATION)
                        .member_id(memberId)
                        .build());
        String refreshToken = jwtGenerator.generateAccessToken(
                MemberJwtPayload.builder()
                        .type(JwtPayload.JwtType.REFRESH)
                        .exp(System.currentTimeMillis() + REFRESH_TOKEN_DURATION)
                        .member_id(memberId)
                        .build());

        return new TokenDTO(accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken) {
        MemberJwtUtil memberJwtUtil = new MemberJwtUtil(refreshToken);

        String accessToken = refreshAccessToken(memberJwtUtil);
        refreshToken = refreshRefreshToken(memberJwtUtil);

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

    private String refreshAccessToken(MemberJwtUtil memberJwtUtil) {
        if (!memberJwtUtil.getPayload().getType().equals(MemberJwtPayload.JwtType.REFRESH)) {
            throw new UnauthorizedException("ACCESS 토큰을 갱신할 수 없습니다.");
        }

        Long memberId = memberJwtUtil.getPayload().getMember_id();
        if(!memberService.existsMember(memberId)) {
            throw new NotFoundException("존재하지 않는 회원입니다.");
        }

        return jwtGenerator.generateAccessToken(
                MemberJwtPayload.builder()
                        .type(JwtPayload.JwtType.ACCESS)
                        .exp(System.currentTimeMillis() + ACCESS_TOKEN_DURATION)
                        .member_id(memberId)
                        .build());
    }

    private String refreshRefreshToken(MemberJwtUtil memberJwtUtil) {
        String refreshToken = memberJwtUtil.getToken();

        if (!memberJwtUtil.checkRefreshToken(REFRESH_TOKEN_DURATION)) {
            refreshToken = jwtGenerator.generateRefreshToken(
                    MemberJwtPayload.builder()
                    .type(JwtPayload.JwtType.REFRESH)
                    .exp(System.currentTimeMillis() + REFRESH_TOKEN_DURATION)
                    .member_id(memberJwtUtil.getPayload().getMember_id())
                    .build());
        }
        return refreshToken;
    }
}


