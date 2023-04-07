package com.ilkda.server.record.service;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.book.repository.BookRepository;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.member.repository.MemberRepository;
import com.ilkda.server.record.model.DailyRecord;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordFinder {

    private final RecordRepository recordRepository;

    private final DailyRecordRepository dailyRecordRepository;

    private final MemberRepository memberRepository;

    private final BookRepository bookRepository;

    private static final Long MAX_READ_COUNT = 5L;

    public Record getRecordById(Long recordId) {
        return recordRepository.findById(recordId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 읽기입니다.");
                });
    }

    public Optional<DailyRecord> getDailyRecordByRegDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return dailyRecordRepository.findByRegDateBetween(fromDate, toDate);
    }

    public List<DailyRecord> getMonthRecord(Long memberId, int year, int month) {
        Member member = getMember(memberId);

        LocalDateTime fromDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime toDate = fromDate.plusMonths(1);
        return dailyRecordRepository.findByMemberAndRegDateBetween(member, fromDate, toDate);
    }

    public Long getMonthReadDateCount(Long memberId, int year, int month) {
        Member member = getMember(memberId);
        LocalDateTime fromDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime toDate = fromDate.plusMonths(1);
        return dailyRecordRepository.countByMemberAndRegDateBetween(member, fromDate, toDate);
    }

    public Long getYearMaxReadPageCount(Long memberId, int year) {
        DailyRecord yearMaxRecord = getDailyRecordTopPageCount(memberId, year, Sort.Direction.DESC);

        return yearMaxRecord != null ? yearMaxRecord.getReadPageCount() : 0L;
    }

    public Long getYearMinReadPageCount(Long memberId, int year) {
        DailyRecord yearMinRecord = getDailyRecordTopPageCount(memberId, year, Sort.Direction.ASC);

        return yearMinRecord != null ? yearMinRecord.getReadPageCount() : 0L;
    }

    /**
     * 연간 읽기 조회는 요청한 연도의 1월 1일 부터 12월 31일 까지의 기록에서 검색합니다.*/
    private DailyRecord getDailyRecordTopPageCount(Long memberId, int year, Sort.Direction direction) {
        Member member = getMember(memberId);
        LocalDateTime fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime toDate = fromDate.plusYears(1);
        Sort sort = Sort.by(direction, "readPageCount");
        return dailyRecordRepository
                .findTopReadPageCountByMemberAndRegDateBetween(member, fromDate, toDate, sort);
    }

    public List<Record> getAllRecordByComplete(Long memberId, boolean complete) {
        Member member = getMember(memberId);

        return recordRepository.findAllByMemberAndComplete(member, complete);
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 회원입니다.");
                });
    }

    public Book getBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 책입니다.");
                });
    }

    public Boolean checkRecordCountLessThanMax(Member member) {
        return !recordRepository.findRecordCountLessThanMax(member.getId(), false, MAX_READ_COUNT);
    }

    public Boolean checkExistsRecordByBookAndMember(Book book, Member member) {
        return recordRepository.existsRecordByBookAndMember(book, member);
    }
}
