package com.ilkda.server.record.service;

import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.service.reader.ClubRecordReader;
import com.ilkda.server.record.service.updater.ClubRecordUpdater;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubRecordService extends RecordService {

    private final ClubRecordReader recordReader;

    public ClubRecordService(ClubRecordReader recordReader, ClubRecordUpdater recordUpdater) {
        super(recordReader, recordUpdater);
        this.recordReader = recordReader;
    }

    public List<Record> getAllReadingRecord(Member member, Long clubId) {
        return recordReader.getAllReadingRecord(member, clubId);
    }
}
