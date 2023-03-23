package com.ilkda.server.record.controller;

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
@RequestMapping("/api/v1/records")
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public SuccessResponse<Long> createRecord(@RequestBody RegisterRecordForm form,
                                              @AuthenticationPrincipal CustomUserDetails user) {
        Long memberId = user.getMemberId();
        return new SuccessResponse<>(
                recordService.createRecord(memberId, form)
        );
    }

    @GetMapping
    public SuccessResponse<List<RecordDTO>> getAllReading(@AuthenticationPrincipal CustomUserDetails user) {
        Long memberId = user.getMemberId();
        return new SuccessResponse<>(
                RecordDTO.getRecordDTOList(recordService.getAllRecordReading(memberId))
        );
    }

    @GetMapping("/history")
    public SuccessResponse<List<RecordDTO>> getAllReadingHistory(@AuthenticationPrincipal CustomUserDetails user) {
        Long memberId = user.getMemberId();
        return new SuccessResponse<>(
                RecordDTO.getRecordDTOList(recordService.getAllRecordHistory(memberId))
        );
    }

    @GetMapping("/{id}")
    public SuccessResponse<RecordDTO> getRecordReading(@PathVariable Long id) {
        Record record = recordService.getRecordReading(id);
        return new SuccessResponse<>(RecordDTO.of(record));
    }

    @GetMapping("/daily/monthly")
    public SuccessResponse<List<DailyRecordDTO>> getMonthRecord(@RequestParam int year,
                                                                @RequestParam int month,
                                                                @AuthenticationPrincipal CustomUserDetails user) {
        Long memberId = user.getMemberId();
        return new SuccessResponse<>(DailyRecordDTO.of(recordService.getMonthRecord(memberId, year, month)));
    }

    @GetMapping("/daily/info")
    public SuccessResponse<DailyRecordInfoDTO> getDailyRecordInfo(@RequestParam int year,
                                                                  @RequestParam int month,
                                                                  @AuthenticationPrincipal CustomUserDetails user) {
        Long memberId = user.getMemberId();
        return new SuccessResponse<>(DailyRecordInfoDTO.builder()
                .yearMaxReadPageCount(recordService.getYearMaxReadPageCount(memberId, year))
                .yearMinReadPageCount(recordService.getYearMinReadPageCount(memberId, year))
                .monthReadDateCount(recordService.getMonthReadDateCount(memberId, year, month))
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
