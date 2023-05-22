package com.ilkda.server.club.repository;

import com.ilkda.server.club.model.Club;
import com.ilkda.server.club.model.ClubMember;
import com.ilkda.server.member.model.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClubMemberRepository extends CrudRepository<ClubMember, Long> {

    Boolean existsByClubAndMember(Club club, Member member);

    List<ClubMember> findByClub(Club club);
}
