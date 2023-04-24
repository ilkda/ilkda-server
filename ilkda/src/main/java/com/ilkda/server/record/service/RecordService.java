package com.ilkda.server.record.service;

import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.dto.RecordTextForm;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.model.DailyRecord;
import com.ilkda.server.record.model.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordReader recordReader;

    private final RecordUpdater recordUpdater;

    public Long createRecord(Member member, RegisterRecordForm form) {
        return recordUpdater.createRecord(member, form);
    }

    public Record getRecordById(Long memberId) {
        return recordReader.getRecordById(memberId);
    }

    public List<Record> getAllRecordReading(Member member) {
        return recordReader.getAllRecordByComplete(member, false);
    }

    public List<Record> getAllRecordHistory(Member member) {
        return recordReader.getAllRecordByComplete(member, true);
    }

    public List<DailyRecord> getMonthRecord(Member member, int year, int month) {
        return recordReader.getMonthRecord(member, year, month);
    }

    public Long getMonthReadDateCount(Member member, int year, int month) {
        return recordReader.getMonthReadDateCount(member, year, month);
    }

    public Long getYearMaxReadPageCount(Member member, int year) {
        return recordReader.getYearMaxReadPageCount(member, year);
    }

    public Long getYearMinReadPageCount(Member member, int year) {
        return recordReader.getYearMinReadPageCount(member, year);
    }

    public Long updateReadPage(Long recordId, Long newPage) {
        return recordUpdater.updateReadPage(recordId, newPage);
    }

    public Long updateText(Long recordId, RecordTextForm form) {
        return recordUpdater.updateText(recordId, form);
    }

    public Long completeRead(Long recordId) {
        return recordUpdater.completeRead(recordId);
    }

}