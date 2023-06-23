package com.ilkda.server.record.repository;

import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.Record;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecordRepository extends CrudRepository<Record, Long> {

    List<Record> findByMemberAndComplete(Member member, Boolean complete);
}
