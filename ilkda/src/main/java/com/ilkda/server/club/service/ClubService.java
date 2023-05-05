package com.ilkda.server.club.service;

import com.ilkda.server.club.dto.CreateClubForm;
import com.ilkda.server.club.model.Club;
import com.ilkda.server.club.repository.ClubRepository;
import com.ilkda.server.member.model.Member;
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

    @Transactional
    public Long createClub(Member member, CreateClubForm form) {
        Club club = Club.builder()
                .leader(member)
                .name(form.getName())
                .build();
        clubRepository.save(club);

        log.info("club#{} 모임 생성 성공", club.getId());
        return club.getId();
    }
}
