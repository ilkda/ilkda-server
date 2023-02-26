package com.ilkda.server.record.controller;

import com.ilkda.server.record.dto.RecordDTO;
import com.ilkda.server.record.dto.RecordTextForm;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.service.RecordService;
import com.ilkda.server.utils.ApiUtil.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/")
    public SuccessResponse<Long> createRecord(@RequestBody RegisterRecordForm form,
                                              Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        return new SuccessResponse<>(
                recordService.createRecord(memberId, form)
        );
    }

    @GetMapping("/")
    public SuccessResponse<List<RecordDTO>> getAllReading(Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();
        return new SuccessResponse<>(
                RecordDTO.getRecordDTOList(recordService.getAllRecordReading(memberId))
        );
    }

    @GetMapping("/{id}")
    public SuccessResponse<RecordDTO> getRecordReading(@PathVariable Long id) {
        Record record = recordService.getRecordReading(id);
        return new SuccessResponse<>(
                RecordDTO.builder()
                        .id(record.getId())
                        .title(record.getBook().getTitle())
                        .author(record.getBook().getAuthor())
                        .bookPage(record.getBook().getPage())
                        .readPage(record.getReadPage())
                        .text(record.getText())
                        .build()
        );
    }

    @PutMapping("/{id}/page")
    public SuccessResponse<Long> updateReadPage(@PathVariable Long id,
                                                @RequestParam Long page) {
        return new SuccessResponse<>(recordService.updateReadPage(id, page));
    }

    @PutMapping("/{id}/text")
    public SuccessResponse<Long> updateRecordText(@PathVariable Long id,
                                                  @RequestBody RecordTextForm form) {
        return new SuccessResponse<>(recordService.updateText(id, form));
    }
}
