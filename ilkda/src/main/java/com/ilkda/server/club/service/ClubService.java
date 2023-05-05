package com.ilkda.server.club.service;

import com.ilkda.server.club.dto.AddMemberForm;
import com.ilkda.server.club.dto.CreateClubForm;
import com.ilkda.server.club.model.Club;
import com.ilkda.server.club.model.ClubMember;
import com.ilkda.server.club.repository.ClubMemberRepository;
import com.ilkda.server.club.repository.ClubRepository;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createClub(Member member, CreateClubForm form) {
        Club club = Club.builder()
                .leader(member)
                .name(form.getName())
                .build();
        clubRepository.save(club);

        addMember(club, member);

        log.info("club#{} 모임 생성 성공", club.getId());
        return club.getId();
    }

    @Transactional
    public void addMember(AddMemberForm form) {
        Club club = getClub(form.getClubId());

        for(Long memberId : form.getMembers()) {
            addMember(club, getMember(memberId));
        }

        log.info("club#{} 모임원 추가 성공", club.getId());
    }

    private void addMember(Club club, Member member) {
        if(memberInClub(club, member)) {
            throw new IllegalStateException("이미 모임에 가입된 회원입니다.");
        }

        ClubMember clubMember = ClubMember.builder()
                .club(club)
                .clubMember(member)
                .build();
        clubMemberRepository.save(clubMember);
    }

    private Club getClub(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 모임입니다.");
                });
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 회원입니다.");
                });
    }

    private boolean memberInClub(Club club, Member member) {
        return clubMemberRepository.existsByClubAndClubMember(club, member);
    }
}
