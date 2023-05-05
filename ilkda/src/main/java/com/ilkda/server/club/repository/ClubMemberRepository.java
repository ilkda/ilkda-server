package com.ilkda.server.club.repository;

import com.ilkda.server.club.model.Club;
import com.ilkda.server.club.model.ClubMember;
import com.ilkda.server.member.model.Member;
import org.springframework.data.repository.CrudRepository;

public interface ClubMemberRepository extends CrudRepository<ClubMember, Long> {

    Boolean existsByClubAndClubMember(Club club, Member member);
}
