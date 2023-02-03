package com.ilkda.server.member.repository;

import com.ilkda.server.member.model.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
}
