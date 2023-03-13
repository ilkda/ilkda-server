package com.ilkda.server.member.repository;

import com.ilkda.server.member.model.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Optional<Member> findByKakaoId(Long kakaoId);
}
