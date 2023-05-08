package com.ilkda.server.record.service.updater;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.DefaultRecord;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.service.reader.DefaultRecordReader;
import org.springframework.stereotype.Service;

@Service
public class DefaultRecordUpdater extends RecordUpdater {

    public DefaultRecordUpdater(DailyRecordRepository dailyRecordRepository, DefaultRecordReader recordReader) {
        super(dailyRecordRepository, recordReader);
    }

    @Override
    protected Record saveRecord(Book book, Member member) {
        return DefaultRecord.builder()
                .book(book)
                .member(member)
                .build();
    }
}
