package com.ilkda.server.record.service.updater;

import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.dto.RecordTextForm;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.model.DailyRecord;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.service.reader.RecordReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public abstract class RecordUpdater {

    private final DailyRecordRepository dailyRecordRepository;

    private final RecordReader recordReader;

    @Transactional
    public abstract void createRecord(Member member, RegisterRecordForm form);

    /**
     * 이전 페이지보다 뒷 페이지로 넘어갔으면 DailyRecord를 추가합니다.
     */
    @Transactional
    public Long updateReadPage(Long recordId, Long newPage) {
        Record record = recordReader.getRecordById(recordId);

        updateDailyRecord(record, newPage);
        record.updateReadPage(newPage);

        log.info("record#{} 읽기 페이지 업데이트 성공", recordId);
        return recordId;
    }

    @Transactional
    public Long updateText(Long recordId, RecordTextForm form) {
        Record record = recordReader.getRecordById(recordId);

        record.updateText(form.getText());

        log.info("record#{} 읽기 감상기록 업데이트 성공", recordId);
        return recordId;
    }

    /**
     * 페이지를 마지막 페이지까가지 업데이트 하지 않고 읽기 마침을 한 경우, 페이지를 마지막 페이지로 업데이트 한 후 읽기를 종료해야 합니다.
     */
    @Transactional
    public Long completeRead(Long recordId) {
        Record record = recordReader.getEachRecordById(recordId);

        if (!record.readLastPage()) {
            updateReadPage(record.getId(), record.getBook().getPage());
        }
        record.completeRead();

        log.info("record#{} 읽기 마침 성공", recordId);
        return recordId;
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
            DailyRecord dailyRecord = recordReader.getDailyRecordByRegDateBetween(fromDate, toDate)
                    .orElse(DailyRecord.builder()
                            .readPageCount(0L)
                            .member(record.getMember())
                            .build());

            Long readPageCount = newPage - oldPage;
            dailyRecord.plusReadPageCount(readPageCount);

            dailyRecordRepository.save(dailyRecord);
        }
    }


}
