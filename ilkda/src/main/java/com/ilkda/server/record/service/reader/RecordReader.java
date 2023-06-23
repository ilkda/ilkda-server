package com.ilkda.server.record.service.reader;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.book.repository.BookRepository;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.DailyRecord;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public abstract class RecordReader {

    private final DailyRecordRepository dailyRecordRepository;

    private final BookRepository bookRepository;

    private final RecordRepository recordRepository;

    public abstract Record getEachRecordById(Long recordId);

    public Record getRecordById(Long recordId) {
        return recordRepository.findById(recordId)
                .orElseThrow(() -> {
                    throw new RuntimeException("존재하지 않는 읽기입니다.");
                });
    }

    public List<Record> getAllCompletedRecord(Member member) {
        return recordRepository.findByMemberAndComplete(member, true);
    }

    public Optional<DailyRecord> getDailyRecordByRegDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return dailyRecordRepository.findByRegDateBetween(fromDate, toDate);
    }

    public List<DailyRecord> getMonthRecord(Member member, int year, int month) {
        LocalDateTime fromDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime toDate = fromDate.plusMonths(1);
        return dailyRecordRepository.findByMemberAndRegDateBetween(member, fromDate, toDate);
    }

    public Long getMonthReadDateCount(Member member, int year, int month) {
        LocalDateTime fromDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime toDate = fromDate.plusMonths(1);
        return dailyRecordRepository.countByMemberAndRegDateBetween(member, fromDate, toDate);
    }

    public Long getYearMaxReadPageCount(Member member, int year) {
        DailyRecord yearMaxRecord = getDailyRecordTopPageCount(member, year, Sort.Direction.DESC);

        return yearMaxRecord != null ? yearMaxRecord.getReadPageCount() : 0L;
    }

    public Long getYearMinReadPageCount(Member member, int year) {
        DailyRecord yearMinRecord = getDailyRecordTopPageCount(member, year, Sort.Direction.ASC);

        return yearMinRecord != null ? yearMinRecord.getReadPageCount() : 0L;
    }

    /**
     * 연간 읽기 조회는 요청한 연도의 1월 1일 부터 12월 31일 까지의 기록에서 검색합니다.*/
    private DailyRecord getDailyRecordTopPageCount(Member member, int year, Sort.Direction direction) {
        LocalDateTime fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime toDate = fromDate.plusYears(1);
        Sort sort = Sort.by(direction, "readPageCount");
        return dailyRecordRepository
                .findTopReadPageCountByMemberAndRegDateBetween(member, fromDate, toDate, sort);
    }

    public Book getBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 책입니다.");
                });
    }
}
