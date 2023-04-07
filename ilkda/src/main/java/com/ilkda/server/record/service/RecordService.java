package com.ilkda.server.record.service;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.dto.RecordTextForm;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.model.DailyRecord;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RecordFinder recordFinder;

    private final RecordRepository recordRepository;

    private final DailyRecordRepository dailyRecordRepository;


    @Transactional
    public Long createRecord(Long memberId, RegisterRecordForm form) {
        Member member = recordFinder.getMember(memberId);
        Book book = recordFinder.getBook(form.getBookId());

        if (moreThanMaxReadCount(member)) {
            throw new IllegalStateException("최대 읽기 수를 초과했습니다");
        }

        if (duplicatedRecord(book, member)) {
            throw new IllegalStateException("이미 존재하는 읽기입니다.");
        }

        Record record = Record.builder()
                .book(book)
                .member(member)
                .build();
        recordRepository.save(record);

        log.info("record#{} 읽기 등록 성공", record.getId());
        return record.getId();
    }

    /**
     * 이전 페이지보다 뒷 페이지로 넘어갔으면 DailyRecord를 추가합니다.
     */
    @Transactional
    public Long updateReadPage(Long recordId, Long newPage) {
        Record record = recordFinder.getRecordById(recordId);

        updateDailyRecord(record, newPage);
        record.updateReadPage(newPage);

        log.info("record#{} 읽기 페이지 업데이트 성공", recordId);
        return recordId;
    }

    @Transactional
    public Long updateText(Long recordId, RecordTextForm form) {
        Record record = recordFinder.getRecordById(recordId);

        record.updateText(form.getText());

        log.info("record#{} 읽기 감상기록 업데이트 성공", recordId);
        return recordId;
    }

    /**
     * 페이지를 마지막 페이지까가지 업데이트 하지 않고 읽기 마침을 한 경우, 페이지를 마지막 페이지로 업데이트 한 후 읽기를 종료해야 합니다.
     */
    @Transactional
    public Long completeRead(Long recordId) {
        Record record = recordFinder.getRecordById(recordId);

        if (!record.readLastPage()) {
            updateReadPage(record.getId(), record.getBook().getPage());
        }
        record.completeRead();

        log.info("record#{} 읽기 마침 성공", recordId);
        return recordId;
    }

    public Record getRecordById(Long memberId) {
        return recordFinder.getRecordById(memberId);
    }

    public List<Record> getAllRecordReading(Long memberId) {
        return recordFinder.getAllRecordByComplete(memberId, false);
    }

    public List<Record> getAllRecordHistory(Long memberId) {
        return recordFinder.getAllRecordByComplete(memberId, true);
    }

    public List<DailyRecord> getMonthRecord(Long memberId, int year, int month) {
        return recordFinder.getMonthRecord(memberId, year, month);
    }

    public Long getMonthReadDateCount(Long memberId, int year, int month) {
        return recordFinder.getMonthReadDateCount(memberId, year, month);
    }

    public Long getYearMaxReadPageCount(Long memberId, int year) {
        return recordFinder.getYearMaxReadPageCount(memberId, year);
    }

    public Long getYearMinReadPageCount(Long memberId, int year) {
        return recordFinder.getYearMinReadPageCount(memberId, year);
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
            DailyRecord dailyRecord = recordFinder.getDailyRecordByRegDateBetween(fromDate, toDate)
                    .orElse(DailyRecord.builder()
                            .readPageCount(0L)
                            .member(record.getMember())
                            .build());

            Long readPageCount = newPage - oldPage;
            dailyRecord.plusReadPageCount(readPageCount);

            dailyRecordRepository.save(dailyRecord);
        }
    }

    private Boolean moreThanMaxReadCount(Member member) {
        return recordFinder.checkRecordCountLessThanMax(member);
    }

    private Boolean duplicatedRecord(Book book, Member member) {
        return recordFinder.checkExistsRecordByBookAndMember(book, member);
    }

}