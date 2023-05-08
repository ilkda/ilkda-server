package com.ilkda.server.record.service.reader;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.book.repository.BookRepository;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.ClubRecordRepository;
import com.ilkda.server.record.repository.DailyRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubRecordReader extends RecordReader {

    private final ClubRecordRepository clubRecordRepository;

    public ClubRecordReader(ClubRecordRepository clubRecordRepository, DailyRecordRepository dailyRecordRepository, BookRepository bookRepository) {
        super(dailyRecordRepository, bookRepository);
        this.clubRecordRepository = clubRecordRepository;
    }

    @Override
    public Record getRecordById(Long recordId) {
        return clubRecordRepository.findById(recordId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 읽기입니다.");
                });
    }

    @Override
    public List<Record> getAllRecordByComplete(Member member, boolean complete) {
        return clubRecordRepository.findAllByMemberAndComplete(member, complete);
    }

    @Override
    public Boolean checkRecordCountLessThanMax(Member member) {
        return !clubRecordRepository.findRecordCountLessThanMax(member.getId(), false, MAX_READ_COUNT);
    }

    @Override
    public Boolean checkExistsRecordByBookAndMember(Book book, Member member) {
        return clubRecordRepository.existsRecordByBookAndMember(book, member);
    }
}
