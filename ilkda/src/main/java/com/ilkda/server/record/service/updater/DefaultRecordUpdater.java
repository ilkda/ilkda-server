package com.ilkda.server.record.service.updater;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.model.DefaultRecord;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.repository.DefaultRecordRepository;
import com.ilkda.server.record.service.reader.DefaultRecordReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultRecordUpdater extends RecordUpdater {

    private final DefaultRecordReader recordReader;

    private final DefaultRecordRepository recordRepository;

    public DefaultRecordUpdater(DailyRecordRepository dailyRecordRepository,
                                DefaultRecordReader recordReader,
                                DefaultRecordRepository recordRepository) {
        super(dailyRecordRepository, recordReader);
        this.recordReader = recordReader;
        this.recordRepository = recordRepository;
    }

    @Override
    public void createRecord(Member member, RegisterRecordForm form) {
        Book book = recordReader.getBook(form.getBookId());

        if (moreThanMaxReadCount(member)) {
            throw new IllegalStateException("최대 읽기 수를 초과했습니다");
        }

        if (duplicatedRecord(book, member)) {
            throw new IllegalStateException("이미 존재하는 읽기입니다.");
        }

        saveRecord(book, member);
    }

    private void saveRecord(Book book, Member member) {
        DefaultRecord record = DefaultRecord.builder()
                .book(book)
                .member(member)
                .build();
        recordRepository.save(record);
    }

    private Boolean moreThanMaxReadCount(Member member) {
        return !recordReader.checkRecordCountLessThanMax(member);
    }

    private Boolean duplicatedRecord(Book book, Member member) {
        return recordReader.checkExistsRecordByBookAndMember(book, member);
    }
}
