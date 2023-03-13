package com.ilkda.server.member.service;

import com.ilkda.server.auth.dto.KakaoUserInfo;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.member.model.Role;
import com.ilkda.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long createUser(KakaoUserInfo kakaoUserInfo) {
        Member member = memberRepository.findByKakaoId(kakaoUserInfo.getId()).orElseGet(() -> {
            Member newMember = Member.builder()
                    .kakaoId(kakaoUserInfo.getId())
                    .nickname(kakaoUserInfo.getKakaoAccount().getProfile().getNickname())
                    .profileImage(kakaoUserInfo.getKakaoAccount().getProfile().getProfile_image_url())
                    .role(Role.ROLE_USER)
                    .build();
            memberRepository.save(newMember);
            return newMember;
        });

       return member.getId();
    }
}
