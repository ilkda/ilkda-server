package com.ilkda.server.record.service.updater;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.ClubRecord;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.service.reader.ClubRecordReader;
import org.springframework.stereotype.Service;

@Service
public class ClubRecordUpdater extends RecordUpdater {

    public ClubRecordUpdater(DailyRecordRepository dailyRecordRepository, ClubRecordReader recordReader) {
        super(dailyRecordRepository, recordReader);
    }

    @Override
    protected Record saveRecord(Book book, Member member) {
        return ClubRecord.builder().build();
    }
}
