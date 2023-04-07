package com.ilkda.server.record.service;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.book.repository.BookRepository;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.member.repository.MemberRepository;
import com.ilkda.server.record.dto.RecordTextForm;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.model.DailyRecord;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private static final Long MAX_READ_COUNT = 5L;

    private final RecordRepository recordRepository;

    private final DailyRecordRepository dailyRecordRepository;

    private final MemberRepository memberRepository;

    private final BookRepository bookRepository;

    @Transactional
    public Long createRecord(Long memberId, RegisterRecordForm form) {
        Member member = findMember(memberId);
        Book book = findBook(form.getBookId());

        if(moreThanMaxReadCount(member)) {
            throw new IllegalStateException("최대 읽기 수를 초과했습니다");
        }

        if(duplicatedRecord(book, member)) {
            throw new IllegalStateException("이미 존재하는 읽기입니다.");
        }

        Record record = Record.builder()
                .book(book)
                .member(member)
                .build();
        recordRepository.save(record);

        return record.getId();
    }

    public List<Record> getAllRecordReading(Long memberId) {
        return getAllRecordByComplete(memberId, false);
    }

    public List<Record> getAllRecordHistory(Long memberId) {
        return getAllRecordByComplete(memberId, true);
    }

    public Record getRecordById(Long recordId) {
        return recordRepository.findById(recordId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 읽기입니다.");
                });
    }

    public List<DailyRecord> getMonthRecord(Long memberId, int year, int month) {
        Member member = findMember(memberId);

        LocalDateTime fromDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime toDate = fromDate.plusMonths(1);
        return dailyRecordRepository.findByMemberAndRegDateBetween(member, fromDate, toDate);
    }

    /**
     * 이전 페이지보다 뒷 페이지로 넘어갔으면 DailyRecord를 추가합니다.*/
    @Transactional
    public Long updateReadPage(Long recordId, Long newPage) {
        Record record = getRecordById(recordId);

        updateDailyRecord(record, newPage);
        record.updateReadPage(newPage);

        return recordId;
    }

    @Transactional
    public Long updateText(Long recordId, RecordTextForm form) {
        Record record = getRecordById(recordId);

        record.updateText(form.getText());
        return recordId;
    }

    /**
     * 페이지를 마지막 페이지까가지 업데이트 하지 않고 읽기 마침을 한 경우, 페이지를 마지막 페이지로 업데이트 한 후 읽기를 종료해야 합니다.*/
    @Transactional
    public Long completeRead(Long recordId) {
        Record record = getRecordById(recordId);

        if(!record.readLastPage()) {
            updateReadPage(record.getId(), record.getBook().getPage());
        }
        record.completeRead();

        return recordId;
    }

    public Long getYearMaxReadPageCount(Long memberId, int year) {
        DailyRecord yearMaxRecord = getYearReadPageCount(memberId, year, Sort.Direction.DESC);

        return yearMaxRecord != null ? yearMaxRecord.getReadPageCount() : 0L;
    }

    public Long getYearMinReadPageCount(Long memberId, int year) {
        DailyRecord yearMinRecord = getYearReadPageCount(memberId, year, Sort.Direction.ASC);

        return yearMinRecord != null ? yearMinRecord.getReadPageCount() : 0L;
    }

    public Long getMonthReadDateCount(Long memberId, int year, int month) {
        Member member = findMember(memberId);
        LocalDateTime fromDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime toDate = fromDate.plusMonths(1);
        return dailyRecordRepository.countByMemberAndRegDateBetween(member, fromDate, toDate);
    }

    /**
     * 읽기 페이지가 이전에 비해 증가한 경우 DailyRecord를 추가합니다.<br>
     * 이미 오늘 DailyRecord가 존재하는 경우, 새 데이터를 추가하는 대신 readPageCount 필드만 업데이트 해야 합니다.
     */
    @Transactional
    protected void updateDailyRecord(Record record, Long newPage) {
        Long oldPage = record.getReadPage();

        if (oldPage < newPage) {
            LocalDateTime fromDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime toDate = fromDate.plusDays(1);
            DailyRecord dailyRecord = dailyRecordRepository.findByRegDateBetween(fromDate, toDate)
                    .orElse(
                            DailyRecord.builder()
                                    .readPageCount(0L)
                                    .member(record.getMember())
                                    .build()
                    );

            Long readPageCount = newPage - oldPage;
            dailyRecord.plusReadPageCount(readPageCount);

            dailyRecordRepository.save(dailyRecord);
        }
    }

    /**
     * 연간 읽기 조회는 요청한 연도의 1월 1일 부터 12월 31일 까지의 기록에서 검색합니다.*/
    private DailyRecord getYearReadPageCount(Long memberId, int year, Sort.Direction direction) {
        Member member = findMember(memberId);
        LocalDateTime fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime toDate = fromDate.plusYears(1);
        Sort sort = Sort.by(direction, "readPageCount");
        return dailyRecordRepository
                .findTopReadPageCountByMemberAndRegDateBetween(member, fromDate, toDate, sort);
    }

    private List<Record> getAllRecordByComplete(Long memberId, boolean complete) {
        Member member = findMember(memberId);

        return recordRepository.findAllByMemberAndComplete(member, complete);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 회원입니다.");
                });
    }

    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 책입니다.");
                });
    }

    private Boolean moreThanMaxReadCount(Member member) {
        return !recordRepository.findRecordCountLessThanMax(member.getId(), false, MAX_READ_COUNT);
    }

    private Boolean duplicatedRecord(Book book, Member member) {
        return recordRepository.existsRecordByBookAndMember(book, member);
    }
}