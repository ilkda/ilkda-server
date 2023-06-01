package com.ilkda.server.record.controller;

import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.dto.*;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.service.RecordService;
import com.ilkda.server.security.details.CustomUserDetails;
import com.ilkda.server.utils.ApiUtil.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public abstract class RecordController {

    private final RecordService recordService;

    public abstract SuccessResponse<String> createRecord(@RequestBody RegisterRecordForm form,
                                                       @AuthenticationPrincipal CustomUserDetails user);

    @GetMapping("/history")
    public SuccessResponse<List<RecordDTO>> getAllReadingHistory(@AuthenticationPrincipal CustomUserDetails user) {
        return new SuccessResponse<>(
                RecordDTO.getRecordDTOList(recordService.getAllRecordHistory(user.getMember()))
        );
    }

    @GetMapping("/api/v1/records/{id}")
    public SuccessResponse<RecordDTO> getRecordReading(@PathVariable Long id) {
        Record record = recordService.getEachRecordById(id);
        return new SuccessResponse<>(RecordDTO.of(record));
    }

    @GetMapping("/daily/monthly")
    public SuccessResponse<List<DailyRecordDTO>> getMonthRecord(@RequestParam int year,
                                                                @RequestParam int month,
                                                                @AuthenticationPrincipal CustomUserDetails user) {
        return new SuccessResponse<>(DailyRecordDTO.of(recordService.getMonthRecord(user.getMember(), year, month)));
    }

    @GetMapping("/daily/info")
    public SuccessResponse<DailyRecordInfoDTO> getDailyRecordInfo(@RequestParam int year,
                                                                  @RequestParam int month,
                                                                  @AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        return new SuccessResponse<>(DailyRecordInfoDTO.builder()
                .yearMaxReadPageCount(recordService.getYearMaxReadPageCount(member, year))
                .yearMinReadPageCount(recordService.getYearMinReadPageCount(member, year))
                .monthReadDateCount(recordService.getMonthReadDateCount(member, year, month))
                .build());
    }

    @PutMapping("/{id}/page")
    public SuccessResponse<Long> updateReadPage(@PathVariable Long id,
                                                @RequestBody RecordPageForm form) {
        return new SuccessResponse<>(recordService.updateReadPage(id, form.getPage()));
    }

    @PutMapping("/{id}/text")
    public SuccessResponse<Long> updateRecordText(@PathVariable Long id,
                                                  @RequestBody RecordTextForm form) {
        return new SuccessResponse<>(recordService.updateText(id, form));
    }

    @PutMapping("/{id}/finish")
    public SuccessResponse<Long> completeRead(@PathVariable Long id) {

        return new SuccessResponse<>(recordService.completeRead(id));
    }
}
