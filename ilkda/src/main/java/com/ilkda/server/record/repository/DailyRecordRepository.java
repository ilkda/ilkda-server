package com.ilkda.server.record.repository;

import com.ilkda.server.record.model.DailyRecord;
import org.springframework.data.repository.CrudRepository;

public interface DailyRecordRepository extends CrudRepository<DailyRecord, Long> {
}
