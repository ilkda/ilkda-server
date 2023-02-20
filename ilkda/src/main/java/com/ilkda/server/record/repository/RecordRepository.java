package com.ilkda.server.record.repository;

import com.ilkda.server.record.model.Record;
import org.springframework.data.repository.CrudRepository;

public interface RecordRepository extends CrudRepository<Record, Long> {
}
