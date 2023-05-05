package com.ilkda.server.club.repository;

import com.ilkda.server.club.model.ClubMember;
import org.springframework.data.repository.CrudRepository;

public interface ClubMemberRepository extends CrudRepository<ClubMember, Long> {
}
