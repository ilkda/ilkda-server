package com.ilkda.server.record.repository;

import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.DailyRecord;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyRecordRepository extends CrudRepository<DailyRecord, Long> {

    List<DailyRecord> findByMemberAndRegDateBetween(Member member, LocalDateTime fromDate, LocalDateTime toDate);
}
