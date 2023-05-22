package com.ilkda.server.record.service.reader;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.book.repository.BookRepository;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.repository.DefaultRecordRepository;
import com.ilkda.server.record.service.RecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultRecordReader extends RecordReader {

    private final DefaultRecordRepository defaultRecordRepository;

    public DefaultRecordReader(DefaultRecordRepository defaultRecordRepository, DailyRecordRepository dailyRecordRepository, BookRepository bookRepository) {
        super(dailyRecordRepository, bookRepository);
        this.defaultRecordRepository = defaultRecordRepository;
    }

    @Override
    public Record getRecordById(Long recordId) {
        return defaultRecordRepository.findById(recordId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 읽기입니다.");
                });
    }

    @Override
    public List<Record> getAllRecordByComplete(Member member, boolean complete) {
        return defaultRecordRepository.findAllByMemberAndComplete(member, complete);
    }

    public Boolean checkRecordCountLessThanMax(Member member) {
        return !defaultRecordRepository.findRecordCountLessThanMax(member.getId(), false, RecordService.MAX_READ_COUNT);
    }

    public Boolean checkExistsRecordByBookAndMember(Book book, Member member) {
        return defaultRecordRepository.existsRecordByBookAndMember(book, member);
    }
}
