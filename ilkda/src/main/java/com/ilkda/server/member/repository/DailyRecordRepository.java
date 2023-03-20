package com.ilkda.server.member.repository;

import com.ilkda.server.member.model.DailyRecord;
import org.springframework.data.repository.CrudRepository;

public interface DailyRecordRepository extends CrudRepository<DailyRecord, Long> {
}
