package com.ilkda.server.member.service;

import com.ilkda.server.auth.dto.KakaoUserInfo;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.member.model.Role;
import com.ilkda.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

            log.info("member#{} 회원 등록 성공", newMember.getId());
            return newMember;
        });

       return member.getId();
    }

    public boolean existsMember(Long memberId) {
        return memberRepository.existsById(memberId);
    }
}
