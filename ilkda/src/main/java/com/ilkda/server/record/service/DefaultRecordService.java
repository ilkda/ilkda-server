package com.ilkda.server.record.service;

import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.service.reader.DefaultRecordReader;
import com.ilkda.server.record.service.updater.DefaultRecordUpdater;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultRecordService extends RecordService {

    private final DefaultRecordReader recordReader;

    public DefaultRecordService(DefaultRecordReader recordReader, DefaultRecordUpdater recordUpdater) {
        super(recordReader, recordUpdater);
        this.recordReader = recordReader;
    }

    public List<Record> getAllReadingRecord(Member member) {
        return recordReader.getAllReadingRecord(member);
    }
}
