package com.ilkda.server.record.repository;

import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.DailyRecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DailyRecordRepository extends CrudRepository<DailyRecord, Long> {

    List<DailyRecord> findByMemberAndRegDateBetween(Member member, LocalDateTime fromDate, LocalDateTime toDate);

    Optional<DailyRecord> findByRegDateBetween(LocalDateTime fromDate, LocalDateTime toDate);

    DailyRecord findTopReadPageCountByMemberAndRegDateBetween(Member member, LocalDateTime fromDate, LocalDateTime toDate, Sort sort);

}
